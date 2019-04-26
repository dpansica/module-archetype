#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.actionbeans;

import ${package}.dto.ContactDTO;
import ${package}.entities.Contact;
import ${groupId}.genericdao.aspects.annotations.ExecuteAtomically;
import ${groupId}.genericdao.jpa.*;
import ${groupId}.webcontroller.actionbean.GenericActionBean;
import ${groupId}.webcontroller.annotation.ServiceMethod;
import ${groupId}.webcontroller.aspects.annotations.ConversionMethod;
import ${groupId}.webcontroller.aspects.annotations.ValidationMethod;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Named("ContactManager")
public class ContactManager extends GenericActionBean
{
	@Inject
	@GenericDAO
	@DAOClass(Contact.class)
	@DBMSUnit(DBMSPersistenceUnit.DBMS1)
	private JpaGenericDAO daoContact;

	@ValidationMethod(serviceName = "getOrSave")
	public Function<Contact, List<String>> validateContactFunction(Contact input) throws Exception{
		return contact -> {

			List<String> errors = new ArrayList<>();

			if (contact.getName()==null) errors.add("name null");

			return errors;
		};
	}

	@ServiceMethod("getOrSave")
	@ExecuteAtomically(DBMSPersistenceUnit.DBMS1)
	public Contact getOrSaveContact(Contact dto)
	{
		List<Contact> result = daoContact.findByExample(dto);

		if(result.size()==0){
			daoContact.save(dto);
		}
		else{
			dto = result.get(0);
		}

		return dto;
	}

	@ConversionMethod(serviceName = "getOrSave")
	public Function<Contact, ContactDTO> convertContactFunction(Contact input) throws Exception{
		return contact -> {
			ContactDTO dto = new ContactDTO();
			dto.setName(contact.getName());
			dto.setAddress(contact.getAddress());

			return dto;
		};
	}


}
