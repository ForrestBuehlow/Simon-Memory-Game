package com.forrestbuehlow.simon;

// Color blind friendly pallets
public enum Color {
	VERMILLION,
	BLUISH_GREEN,
	YELLOW,
	BLUE;

	public java.awt.Color getColor() {
		switch(this) {
			case VERMILLION:
				return java.awt.Color.decode("0xd55e00");
			case BLUISH_GREEN:
				return java.awt.Color.decode("0x009e73");
			case YELLOW:
				return java.awt.Color.decode("0xf0e442");
			case BLUE:
				return java.awt.Color.decode("0x0072B2");
			default:
				return null;
		}
	}
}
