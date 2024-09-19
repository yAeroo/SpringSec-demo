package com.nexus.demo.SpringSecDemo.Controller;

import com.nexus.demo.SpringSecDemo.Model.AppUser;
import com.nexus.demo.SpringSecDemo.Model.RegisterDTO;
import com.nexus.demo.SpringSecDemo.Repository.RoleRepository;
import com.nexus.demo.SpringSecDemo.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping({"", "/"})
    public String index(){
        return "index";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(){
        return "userDashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(){
        return "adminDashboard";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model){
        if (request.getUserPrincipal() != null) { // Si el usuario ya está autenticado
            return "redirect:/"; // Redirige a la página principal o al dashboard
        }
        return  "login";
    }

    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request){
        if (request.getUserPrincipal() != null) { // Si el usuario ya está autenticado
            return "redirect:/"; // Redirige a la página principal o al dashboard
        }

        RegisterDTO registerDTO = new RegisterDTO();
        model.addAttribute("registerDTO", registerDTO);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("success", false);
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegisterDTO registerDTO,
            BindingResult bindingResult,
            Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("roles", roleRepository.findAll());
            return "register";
        }

        if(!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())){
            bindingResult.addError(new FieldError("registerDTO", "password", "Las contraseñas no coinciden."));

            model.addAttribute("roles", roleRepository.findAll());
            return "register";
        }

        AppUser appUser = userRepository.findByUsername(registerDTO.getUsername());
        if(appUser != null){
            bindingResult.addError(
                    new FieldError("registerDTO", "username", "El nombre de usuario ya se encuentra registrado.")
            );

            model.addAttribute("roles", roleRepository.findAll());
            return "register";
        }

        // Si nada dio error entonces...
        try {
            // Creando nueva cuenta
            var bCryptEncoder = new BCryptPasswordEncoder();

            AppUser newAppUser = new AppUser();
            newAppUser.setUsername(registerDTO.getUsername()); // Nombre de usuario
            newAppUser.setPassword(bCryptEncoder.encode(registerDTO.getPassword())); // Contraseña encriptada
            newAppUser.setRole(roleRepository.findById(registerDTO.getRole_id())); // Creando el rol

            userRepository.save(newAppUser); // Guardando el nuevo usuario

            model.addAttribute("registerDTO", new RegisterDTO()); // Limpiando el DTO
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("success", true);

        }catch (Exception e){
            bindingResult.addError(
                    new FieldError("registerDTO", "username", e.getMessage())
            );
        }

        model.addAttribute("roles", roleRepository.findAll());
        return "register";
    }

}
