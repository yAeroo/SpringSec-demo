package com.nexus.demo.SpringSecDemo.Model;

import jakarta.validation.constraints.*;

public class RegisterDTO {
    @NotEmpty(message = "El nombre de usuario no debe estar vacío.")
    private String username;

    @NotEmpty(message = "La contraseña no debe estar vacía.")
    @Size(min = 6, message = "La contraseña debe contener al menos 6 caracteres.")
    private String password;

    private String confirmPassword;

    private int role_id;

    public @NotEmpty(message = "El nombre de usuario no debe estar vacío.") String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty(message = "El nombre de usuario no debe estar vacío.") String username) {
        this.username = username;
    }

    public @NotEmpty(message = "La contraseña no debe estar vacía.") @Size(min = 6, message = "La contraseña debe contener al menos 6 caracteres.") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "La contraseña no debe estar vacía.") @Size(min = 6, message = "La contraseña debe contener al menos 6 caracteres.") String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
}
