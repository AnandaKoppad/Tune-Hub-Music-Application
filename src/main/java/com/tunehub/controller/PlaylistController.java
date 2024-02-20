package com.tunehub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tunehub.entities.Playlist;
import com.tunehub.entities.Song;
import com.tunehub.services.PlaylistService;
import com.tunehub.services.SongService;



@Controller
public class PlaylistController 
{

	@Autowired
	SongService songService;

	@Autowired
	PlaylistService playlistService;

	@GetMapping("/createPlaylist")
	public String createPlaylist(Model model)
	{
		List<Song> songList= songService.fetchAllSongs();
		model.addAttribute("songs", songList);
		return "createPlaylist";
	}


	@PostMapping("/addPlaylist")
	public String addPlaylist(@ModelAttribute Playlist playlist)
	{
		//Updating Play List table
		playlistService.addPlaylist(playlist);

		//Updating Song Table
		List<Song> songList=playlist.getSongs();
		for(Song s:songList)
		{
			s.getPlaylists().add(playlist);
			songService.updateSong(s);
		}

		return "adminhome";
	}


	@GetMapping("/viewPlaylists")
	public String getMethodName(Model model) 
	{
		List<Playlist> allPlaylist= playlistService.fetchAllPlaylists();
		model.addAttribute("allPlaylists", allPlaylist);
		return "displayPlaylists";
	}

}
