package br.com.project.services;

import br.com.project.controllers.PersonController;
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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

	private final Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	private PersonRepository repository;

	@Autowired
	private PersonMapper personMapper;

	public List<PersonVO> findAll() {
		logger.info("Finding all people");
		List<PersonVO> persons = parseListObjects(repository.findAll(), PersonVO.class);
		persons.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		return persons;
	}

	public PersonVO findById(Long id) {
		logger.info("Finding one person");
		Person entity = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("No records found fot his ID!"));

		PersonVO vo = parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
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
		Person entity = repository.findById(person.getKey()).orElseThrow(
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