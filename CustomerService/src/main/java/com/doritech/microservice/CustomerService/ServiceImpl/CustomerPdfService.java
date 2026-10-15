package com.doritech.microservice.CustomerService.ServiceImpl;

import com.doritech.microservice.CustomerService.Entity.Contact;
import com.doritech.microservice.CustomerService.Entity.Customer;
import com.doritech.microservice.CustomerService.Entity.CustomerAddress;
import com.doritech.microservice.CustomerService.Repository.CustomerRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerPdfService {

    private static final Logger log = LoggerFactory.getLogger(CustomerPdfService.class);

    // ---------------------------------------------------------------------
    // Configuration
    // ---------------------------------------------------------------------

    /** Envelope background image, placed in src/main/resources/ */
    private static final String BACKGROUND_RESOURCE = "envelope-bg.png";

    /** Original image size in pixels. All coordinates below are measured on this size. */
    private static final float IMG_PX_W = 2576f;
    private static final float IMG_PX_H = 1304f;

    /** Address block position, in image pixels (measured from the envelope's top-left). */
    private static final float ADDR_LEFT_PX   = 450f;   // just right of the "To," text
    private static final float ADDR_RIGHT_PX  = 1650f;
    private static final float ADDR_TOP_PX    = 415f;   // first line lines up with "To,"
    private static final float ADDR_BOTTOM_PX = 915f;   // stays above the From divider line

    /** Single-envelope PDF: page is exactly the envelope size (~DL, 220 mm). */
    private static final float SINGLE_W = 624f;
    private static final float SINGLE_H = SINGLE_W * IMG_PX_H / IMG_PX_W;

    /** Batch PDF: envelopes stacked on A4. 3 x ~253pt fits on A4. */
    private static final int   PER_PAGE = 3;
    private static final float LIST_W   = 500f;
    private static final float LIST_H   = LIST_W * IMG_PX_H / IMG_PX_W;
    private static final float LIST_GAP = 10f;

    /** Font sizes are defined for a 500pt-wide envelope and scaled for other widths. */
    private static final float BASE_WIDTH   = 500f;
    private static final float BOLD_SIZE    = 9f;
    private static final float NORMAL_SIZE  = 8.5f;
    private static final float LEADING      = 11f;

    @Autowired
    private CustomerRepository customerRepository;

    // ---------------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------------

    /** One envelope for one customer, on a page the size of the envelope. */
    @Transactional(readOnly = true)
    public byte[] generateCustomerPdf(String customerCode) throws DocumentException, IOException {
        Document document = new Document(new Rectangle(SINGLE_W, SINGLE_H), 0, 0, 0, 0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean ok = false;

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            Customer customer = customerRepository.findByCustomerCode(customerCode).orElse(null);
            if (customer == null) {
                addMessage(document, "Customer not found!");
            } else {
                float fontScale = SINGLE_W / BASE_WIDTH;
                Phrase address = buildAddressPhrase(customer, fontScale);
                if (address == null) {
                    addMessage(document, "Customer does not have AddressLine2, skipping envelope.");
                } else {
                    Image bg = loadBackground();
                    drawEnvelope(writer, bg, address, 0, SINGLE_H, SINGLE_W, SINGLE_H, fontScale);
                }
            }
            ok = true;
        } finally {
            closeSafely(document, ok);
        }

        return out.toByteArray();
    }

    /** All customers with a valid address, PER_PAGE envelopes per A4 page. */
    @Transactional(readOnly = true)
    public byte[] generateCustomerListPdf() throws DocumentException, IOException {
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean ok = false;

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            List<Customer> validCustomers = customerRepository.findAll().stream()
                    .filter(c -> hasValidAddress(c))
                    .collect(Collectors.toList());

            if (validCustomers.isEmpty()) {
                addMessage(document, "No customers with a valid address found.");
            } else {
                createMultipleEnvelopesOnPage(document, writer, validCustomers);
            }
            ok = true;
        } finally {
            closeSafely(document, ok);
        }

        return out.toByteArray();
    }

    /**
     * Closes the document. If the work above already failed, a "document has no pages" error from
     * close() is swallowed so the ORIGINAL exception (e.g. missing background image) is what you see.
     */
    private void closeSafely(Document document, boolean succeeded) {
        try {
            document.close();
        } catch (RuntimeException e) {
            if (succeeded) throw e;
        }
    }

    // ---------------------------------------------------------------------
    // Layout
    // ---------------------------------------------------------------------

    private void createMultipleEnvelopesOnPage(Document document, PdfWriter writer, List<Customer> customers)
            throws DocumentException, IOException {

        Image bg = loadBackground();                       // loaded once, reused for every envelope
        float fontScale = LIST_W / BASE_WIDTH;

        Rectangle page = document.getPageSize();
        float x = (page.getWidth() - LIST_W) / 2f;         // horizontally centred
        float blockH = PER_PAGE * LIST_H + (PER_PAGE - 1) * LIST_GAP;
        float startTop = (page.getHeight() + blockH) / 2f; // vertically centred block

        int slot = 0;
        for (Customer customer : customers) {
            Phrase address = buildAddressPhrase(customer, fontScale);
            if (address == null) continue;

            if (slot == PER_PAGE) {
                document.newPage();
                slot = 0;
            }

            float top = startTop - slot * (LIST_H + LIST_GAP);
            drawEnvelope(writer, bg, address, x, top, LIST_W, LIST_H, fontScale);
            slot++;
        }
    }

    /** Draws the background, then the address on top. (x, top) is the envelope's top-left corner. */
    private void drawEnvelope(PdfWriter writer, Image bg, Phrase address,
                              float x, float top, float width, float height, float fontScale)
            throws DocumentException {

        float px = width / IMG_PX_W;                       // points per image pixel

        // 1. Background, drawn UNDER the text
        bg.scaleAbsolute(width, height);
        bg.setAbsolutePosition(x, top - height);
        writer.getDirectContentUnder().addImage(bg);

        // 2. Address block
        ColumnText ct = new ColumnText(writer.getDirectContent());
        ct.setSimpleColumn(
                x + ADDR_LEFT_PX * px,
                top - ADDR_BOTTOM_PX * px,
                x + ADDR_RIGHT_PX * px,
                top - ADDR_TOP_PX * px);
        ct.setLeading(LEADING * fontScale);
        ct.addText(address);
        ct.go();

        // Content was drawn directly on the canvas, so iText still thinks the page is empty
        // and would silently ignore the next document.newPage(). This prevents that.
        writer.setPageEmpty(false);
    }

    private Image loadBackground() throws IOException, BadElementException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(BACKGROUND_RESOURCE)) {
            if (is == null) {
                throw new IOException(BACKGROUND_RESOURCE + " not found in src/main/resources");
            }
            return Image.getInstance(is.readAllBytes());   // readAllBytes needs Java 9+
        }
    }

    private void addMessage(Document document, String text) throws DocumentException {
        Paragraph p = new Paragraph(text);
        p.setIndentationLeft(20);
        p.setSpacingBefore(20);
        document.add(p);
    }

    // ---------------------------------------------------------------------
    // Data helpers
    // ---------------------------------------------------------------------

    private Contact findPrimaryContact(Customer customer) {
        return customer.getContacts().stream()
                .filter(c -> Boolean.TRUE.equals(c.getIsPrimary()))
                .findFirst()
                .orElse(customer.getContacts().stream().findFirst().orElse(null));
    }

    private CustomerAddress findAddress(Customer customer) {
        return customer.getAddresses().stream()
                .filter(a -> a.getType() == CustomerAddress.AddressType.REGISTERED)
                .findFirst()
                .orElse(customer.getAddresses().stream().findFirst().orElse(null));
    }

    private boolean hasValidAddress(Customer customer) {
        CustomerAddress addr = findAddress(customer);
        return addr != null && !getValueOrEmpty(addr.getAddressLine2()).trim().isEmpty();
    }

    /** Builds the address text, or returns null if the customer has no valid address. */
    private Phrase buildAddressPhrase(Customer customer, float fontScale) {
        Contact contact = findPrimaryContact(customer);
        CustomerAddress address = findAddress(customer);

        if (address == null || getValueOrEmpty(address.getAddressLine2()).trim().isEmpty()) {
            return null;
        }

        Font bold   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, BOLD_SIZE * fontScale, BaseColor.BLACK);
        Font normal = FontFactory.getFont(FontFactory.HELVETICA, NORMAL_SIZE * fontScale, BaseColor.BLACK);

        log.info("Envelope data [{}]: contactName='{}', designation='{}', organisation='{}', department='{}', "
                        + "line1='{}', line2='{}', city='{}', state='{}', pin='{}', mobile='{}'",
                customer.getCustomerCode(),
                getContactName(contact),
                contact != null ? contact.getDesignation() : null,
                customer.getName(),
                contact != null ? contact.getDepartment() : null,
                address.getAddressLine1(), address.getAddressLine2(),
                address.getCity(), address.getState(), address.getPostalCode(),
                contact != null ? contact.getMobileNumber() : null);

        Phrase p = new Phrase();
        appendLine(p, getContactName(contact), bold);
        appendLine(p, getValueOrEmpty(contact != null ? contact.getDesignation() : null), bold);
        appendLine(p, getValueOrEmpty(customer.getName()), bold);
        appendLine(p, getValueOrEmpty(contact != null ? contact.getDepartment() : null), bold);
        appendLine(p, getValueOrEmpty(address.getAddressLine1()), normal);
        appendLine(p, getValueOrEmpty(address.getAddressLine2()), normal);

        String city  = getValueOrEmpty(address.getCity()).trim();
        String state = getValueOrEmpty(address.getState()).trim();
        String pin   = getValueOrEmpty(address.getPostalCode()).trim();
        String line  = (city + " " + state).trim() + (pin.isEmpty() ? "" : " - " + pin);
        appendLine(p, line.trim(), normal);

        String mobile = getValueOrEmpty(contact != null ? contact.getMobileNumber() : null);
        if (!mobile.isEmpty()) {
            appendLine(p, "Mobile No: " + mobile, bold);
        }

        return p;
    }

    private void appendLine(Phrase phrase, String text, Font font) {
        if (text != null && !text.trim().isEmpty()) {
            phrase.add(new Chunk(text.trim() + "\n", font));
        }
    }

    private String getContactName(Contact contact) {
        if (contact == null) return "";
        String prefix = "";
        if (contact.getGender() != null) {
            String g = contact.getGender().toString();
            if ("MALE".equalsIgnoreCase(g) || "M".equalsIgnoreCase(g)) prefix = "Capt. ";
            else if ("FEMALE".equalsIgnoreCase(g) || "F".equalsIgnoreCase(g)) prefix = "Smt. ";
        }
        String name;
        if (contact.getFullName() != null && !contact.getFullName().trim().isEmpty()) {
            name = contact.getFullName();
        } else {
            String firstName = contact.getFirstName() != null ? contact.getFirstName() : "";
            String lastName = contact.getLastName() != null ? contact.getLastName() : "";
            name = (firstName + " " + lastName).trim();
        }
        return (prefix + name).trim();
    }

    private String getValueOrEmpty(String value) {
        return (value != null && !value.trim().isEmpty()) ? value : "";
    }
}