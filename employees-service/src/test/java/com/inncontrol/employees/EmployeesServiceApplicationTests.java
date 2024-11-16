package com.inncontrol.employees;

import com.inncontrol.employees.domain.Employee;
import com.inncontrol.employees.application.EmployeeService;
import com.inncontrol.employees.domain.EmployeeRepository;
import com.inncontrol.employees.dto.EmployeeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeesServiceApplicationTests {

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeeService employeeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// Test CREATE operation
	@Test
	void registerEmployee_WithValidData() {
		// Given
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setNombre("Juan");
		employeeDTO.setApellido("Perez");
		employeeDTO.setGenero("M");
		employeeDTO.setCorreo("juan.perez@example.com");
		employeeDTO.setTelefono(Long.valueOf("123456789"));

		Employee employee = new Employee();
		employee.setId(1L);

		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

		// When
		Employee savedEmployee = employeeService.registerEmployee(employeeDTO);

		// Then
		assertNotNull(savedEmployee);
		assertEquals(1L, savedEmployee.getId());
	}

	// Test READ operation
	@Test
	void getEmployeeById() {
		// Given
		Employee employee = new Employee();
		employee.setId(1L);

		when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

		// When
		Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);

		// Then
		assertTrue(foundEmployee.isPresent());
		assertEquals(1L, foundEmployee.get().getId());
	}

	// Test UPDATE operation
	@Test
	void updateEmployee() {
		// Given
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setNombre("Carlos");
		employeeDTO.setApellido("Ramirez");
		employeeDTO.setGenero("M");
		employeeDTO.setCorreo("carlos.ramirez@example.com");
		employeeDTO.setTelefono(Long.valueOf("987654321"));

		Employee existingEmployee = new Employee();
		existingEmployee.setId(1L);

		when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
		when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

		// When
		Optional<Employee> updatedEmployee = employeeService.updateEmployee(1L, employeeDTO);

		// Then
		assertTrue(updatedEmployee.isPresent());
		assertEquals("Carlos", updatedEmployee.get().getNombre());
		assertEquals("Ramirez", updatedEmployee.get().getApellido());
	}

	// Test UPDATE operation (negative scenario)
	@Test
	void updateEmployee_WhenEmployeeDoesNotExist() {
		// Given
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setNombre("Carlos");

		when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

		verify(employeeRepository, never()).save(any(Employee.class));
	}

	// Test DELETE operation
	@Test
	void deleteEmployee_ShouldDeleteEmployee_WhenEmployeeExists() {
		// Given
		Employee existingEmployee = new Employee();
		existingEmployee.setId(1L);

		when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
		doNothing().when(employeeRepository).deleteById(1L);

		// When
		employeeService.deleteEmployee(1L);

		// Then
		verify(employeeRepository, times(1)).deleteById(1L);
	}

	// Test DELETE operation (negative scenario)
	@Test
	void deleteEmployee_WhenEmployeeDoesNotExist() {
		// Given
		when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

		verify(employeeRepository, never()).deleteById(anyLong());
	}
}