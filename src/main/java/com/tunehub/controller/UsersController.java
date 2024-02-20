package com.tunehub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tunehub.entities.Song;
import com.tunehub.entities.Users;
import com.tunehub.services.SongService;
import com.tunehub.services.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
//@CrossOrigin("*")          // Use After Adding the Login Data Class and Connect with React that time only used
//@RestController            // Connect React that Time we are use @RestController
public class UsersController {
	@Autowired
	UsersService service;
	
	@Autowired
	SongService songService;

	@PostMapping("/register")
	public String addUsers(@ModelAttribute Users user)
	{
		boolean userStatus=service.emailExists(user.getEmail());
		if(userStatus==false) 
		{
			service.addUser(user);
			System.out.println("User Added");
		}
		else
		{
			System.out.println("User Already exist");
		}
		return "home";

	}

	@PostMapping("/validate")
                                                                                                  //Before Adding the Login Data 
	public String validate( @RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, Model model)
//	public String validate(@RequestBody LoginData data, HttpSession session, Model model)         //After Adding the Login Data Class
	{
		System.out.println("call received");

//		String email = data.getEmail();                      //After Adding the Login Data Class
//		String password = data.getPassword();                //After Adding the Login Data Class
		
		if(service.validateUser(email,password) == true)
		{
			String role=service.getRole(email);

			session.setAttribute("email", email);

			if(role.equals("admin"))
			{
				return "adminhome";
			}
			else
			{
				Users user=service.getUser(email);
				boolean userStatus=user.isPremium();
				List<Song> songList = songService.fetchAllSongs();
				model.addAttribute("songs",songList);
				model.addAttribute("isPremium", userStatus);
				
				return "customerhome";
			}

		}
		else
		{
			return "login";
		}
	}

//
//	@GetMapping("/pay")
//	public String pay(@RequestParam String email)
//	{ 
//		boolean paymentStatus=false;   //Payment api
//
//		if(paymentStatus==true)
//		{
//			Users user = service.getUser(email);
//			user.setPremium(true);
//			service.updateUser(user);
//		}
//
//		return "login";
//	}

	@GetMapping("/logout")
	public String logout(HttpSession session)
	{
		session.invalidate();
		return "login";
	}

}
