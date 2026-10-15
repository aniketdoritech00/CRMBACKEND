package com.doritech.microservice.CustomerService.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.microservice.CustomerService.Entity.CustomerGenerator;
import com.doritech.microservice.CustomerService.Repository.CustomerGeneratorRepository;

@Service
public class CustomerGeneratorService {

	@Autowired
	private CustomerGeneratorRepository repository;

	/**
	 * Get customer value by merging prefix and suffix
	 * 
	 * @param id - ID of customer generator record
	 * @return Generated customer value (prefix + suffix)
	 */
	public String getCustomerValue(Integer id) {
		CustomerGenerator generator = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer Generator not found with id: " + id));

		return generator.getPrefix() + generator.getSuffix();
	}

	/**
	 * Update suffix by incrementing it and return new customer value
	 * 
	 * @param id - ID of customer generator record
	 * @return Updated customer value (prefix + new suffix)
	 */
	@Transactional
	public String updateAndGetCustomerValue(Integer id) {
		CustomerGenerator generator = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer Generator not found with id: " + id));

		String currentSuffix = generator.getSuffix();
		try {
			int suffixNum = Integer.parseInt(currentSuffix);
			suffixNum++;
			String newSuffix = String.format("%0" + currentSuffix.length() + "d", suffixNum);
			generator.setSuffix(newSuffix);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Suffix is not a valid number: " + currentSuffix);
		}

		repository.save(generator);

		return generator.getPrefix() + generator.getSuffix();
	}
}