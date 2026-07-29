package ru.pulsecore.user_service.service.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.pulsecore.user_service.domain.Role;
import ru.pulsecore.user_service.repository.RoleRepository;

import static org.junit.jupiter.api.Assertions.*;

class RoleServiceTest {


    private RoleRepository  repository;

    private RoleService service;

    @BeforeEach
    void  init() {
        repository = Mockito.mock(RoleRepository.class);
        service = new RoleService(repository);
    }


    @Test
    void shouldFindRoleUser() {
        // Given — что вернёт мок, когда его вызовут
        Role expectedRole = new Role();
        expectedRole.setName("ROLE_USER");
        Mockito.when(repository.findByName("ROLE_USER")).thenReturn(expectedRole);

        // When — вызываем настоящий метод
        Role actualRole = service.findRoleUser();

        // Then — проверяем
        assertEquals("ROLE_USER", actualRole.getName());           // имя совпало
        Mockito.verify(repository).findByName("ROLE_USER");        // мок был вызван
    }

    @Test
    void shouldSaveRoleUser() {
        // Given
        Role roleToSave = new Role();
        roleToSave.setName("ROLE_USER");
        Mockito.when(repository.save(roleToSave)).thenReturn(roleToSave);

        // When
        Role actualRole = service.save(roleToSave);

        // Then
        assertEquals("ROLE_USER", actualRole.getName());
        Mockito.verify(repository).save(roleToSave);
    }

    @Test
    void shouldReturnNullWhenRoleNotFound() {
        // Given — мок возвращает null
        Mockito.when(repository.findByName("ROLE_USER")).thenReturn(null);

        // When — вызываем метод
        Role actualRole = service.findRoleUser();

        // Then — проверяем, что вернулся null
        assertNull(actualRole);
        Mockito.verify(repository).findByName("ROLE_USER");
    }




}