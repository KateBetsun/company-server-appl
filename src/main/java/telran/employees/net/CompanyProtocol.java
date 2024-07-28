package telran.employees.net;

import java.util.*;
import java.util.stream.Collectors;

import telran.employees.*;
import telran.net.*;


public class CompanyProtocol implements Protocol {
	Company company;
	
	public CompanyProtocol(Company company) {
		this.company = company;
	}

	@Override
	public Response getResponse(Request request) {
		String requestType = request.requestType();
		String requestData = request.requestData();
		Response response = null;
		try {
			response = switch(requestType) {
			case "addEmployee" -> addEmployee(requestData);
			case "getEmployee" -> getEmployee(requestData);
			case "removeEmployee" -> removeEmployee(requestData);
			case "getDepartmentBudget" -> getDepartmentBudget(requestData);
			case "getDepartments" -> getDepartments(requestData);
			case "getManagersWithMostFactor" -> getManagersWithMostFactor(requestData);
			default -> wrongTypeResponse(requestType);
			};
			
		} catch (Exception e) {
			response = wrongDataResponse(e.getMessage());
		}
		return response;
	}

	private Response wrongDataResponse(String message) {
		
		return new Response(ResponseCode.WRONG_REQUEST_DATA, message);
	}

	private Response wrongTypeResponse(String requestType) {
		
		return new Response(ResponseCode.WRONG_REQUEST_TYPE, requestType);
	}
	

	private Response getManagersWithMostFactor(String requestData) {
		Manager[] managers = company.getManagersWithMostFactor();
		return new Response(ResponseCode.OK, managersToJSON(managers));
	}

	private String managersToJSON(Manager[] managers) {
		
		return Arrays.stream(managers)
				.map(Employee::getJSON)
				.collect(Collectors.joining(";"));
	}


	private Response getDepartments(String requestData) {
		String[] departments = company.getDepartments();
		return new Response(ResponseCode.OK, departmentsToJSON(departments));
	}

	private String departmentsToJSON(String[] departments) {
		return Arrays.stream(departments)
				.map(depart -> "\"" + depart + "\"")
				.collect(Collectors.joining(", ", "[", "]"));
	}

	private Response getDepartmentBudget(String requestData) {
		int budget = company.getDepartmentBudget(requestData);	
		return new Response(ResponseCode.OK, String.valueOf(budget));
	}

	private Response removeEmployee(String requestData) {
		long id =Long.parseLong(requestData);
		company.removeEmployee(id);
		return new Response(ResponseCode.OK, "");
	}

	private Response getEmployee(String requestData) {
		long id =Long.parseLong(requestData);
		company.getEmployee(id);
		return new Response(ResponseCode.OK, "");
	}

	private Response addEmployee(String emplJSON) {
		Employee empl = (Employee) new Employee().setObject(emplJSON);
		company.addEmployee(empl);
		return new Response(ResponseCode.OK, "");
	}


}
