package br.com.project.services;

import br.com.project.data.vo.v1.PersonVO;
import br.com.project.data.vo.v2.PersonVOV2;
import br.com.project.exceptions.ResourceNotFoundException;
import br.com.project.mapper.custom.PersonMapper;
import br.com.project.model.Person;
import br.com.project.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static br.com.project.mapper.DozerMapper.parseListObjects;
import static br.com.project.mapper.DozerMapper.parseObject;

@Service
public class PersonServices {

	private final Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	private PersonRepository repository;

	@Autowired
	private PersonMapper personMapper;

	public List<PersonVO> findAll() {
		logger.info("Finding all people");
		return parseListObjects(repository.findAll(), PersonVO.class);
	}

	public PersonVO findById(Long id) {
		logger.info("Finding one person");
		Person entity = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("No records found fot his ID!"));

		return parseObject(entity, PersonVO.class);
	}

	public PersonVO create(PersonVO person) {
		logger.info("Creating one person");
		Person entity = parseObject(person, Person.class);
		return parseObject(repository.save(entity), PersonVO.class);
	}

	public PersonVOV2 createV2(PersonVOV2 person) {
		logger.info("Creating one person");
		Person entity = personMapper.convertVoToEntity(person);
		return personMapper.convertEntityToVo(repository.save(entity));
	}

	public PersonVO update(PersonVO person) {
		logger.info("Updating one person");
		Person entity = repository.findById(person.getId()).orElseThrow(
				() -> new ResourceNotFoundException("No records found fot his ID!"));

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());

		return parseObject(repository.save(entity), PersonVO.class);
	}

	public void delete(Long id) {
		logger.info("Deleting one person");
		Person entity = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("No records found fot his ID!"));

		repository.delete(entity);
	}
}