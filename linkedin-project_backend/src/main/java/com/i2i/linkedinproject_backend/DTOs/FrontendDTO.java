package com.i2i.linkedinproject_backend.DTOs;

import com.i2i.linkedinproject_backend.models.Player;

/*
 * Description: This class represent a POJO used to represent data coming from and to the frontend. Holds a Player object and a GameSessionDTO object. 
 * 
 * @author Arhian Albis Ramos
 */
public class FrontendDTO {
	private GameSessionDTO currentSessionDTO;
	private Player currentPlayer;
	
	public GameSessionDTO getCurrentSessionDTO() {
		return currentSessionDTO;
	}
	
	public void setCurrentSessionDTO(GameSessionDTO currentSessionDTO) {
		this.currentSessionDTO = currentSessionDTO;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public FrontendDTO(GameSessionDTO currentSessionDTO, Player currentPlayer) {
		this.currentSessionDTO = currentSessionDTO;
		this.currentPlayer = currentPlayer;
	}
	
	public FrontendDTO() {
	}
	
	public static FrontendDTO build(GameSessionDTO currentSessionDTO, Player currentPlayer) {
		return new FrontendDTO(currentSessionDTO, currentPlayer);
	}
	
	public String toString() {
		return "FrontendDTO [currentSessionDTO=" + currentSessionDTO.toString() + ", currentPlayer=" + currentPlayer.toString() + "]";
	}
}
