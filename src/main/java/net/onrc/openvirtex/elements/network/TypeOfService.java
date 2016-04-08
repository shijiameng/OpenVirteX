package net.onrc.openvirtex.elements.network;

public enum TypeOfService {
	DROP_LOW_PREC((byte) (1 << 4)),
	DROP_MEDIUM_PREC((byte) (2 << 4)),
	DROP_HIGH_PREC((byte) (3 << 4)),
	
	AF1((byte) 0x20, 1, 0.4),
	AF11((byte) (AF1.value() | DROP_LOW_PREC.value())),
	AF12((byte) (AF1.value() | DROP_MEDIUM_PREC.value())),
	AF13((byte) (AF1.value() | DROP_HIGH_PREC.value())),
	AF2((byte) 0x40, 2, 0.3),
	AF21((byte) (AF2.value() | DROP_LOW_PREC.value())),
	AF22((byte) (AF2.value() | DROP_MEDIUM_PREC.value())),
	AF23((byte) (AF2.value() | DROP_HIGH_PREC.value())),
	AF3((byte) 0x60, 3, 0.3),
	AF31((byte) (AF3.value() | DROP_LOW_PREC.value())),
	AF32((byte) (AF3.value() | DROP_MEDIUM_PREC.value())),
	AF33((byte) (AF3.value() | DROP_HIGH_PREC.value()));
	
	
	private byte value;
	private int weight;
	private double initPenalty;
	
	private TypeOfService(final byte value, final int weight, final double initPenalty) {
		this.value = value;
		this.weight = weight;
		this.initPenalty = initPenalty;
	}
	
	private TypeOfService(final byte value) {
		this.value = value;
	}
	
	public static TypeOfService valueOf(byte value) {
		switch (value) {
		case 0x20:
			return AF1;
		case 0x40:
			return AF2;
		case 0x60:
			return AF3;
		default:
			return null;
		}
	}
	
	public byte value() {
		return this.value;
	}
	
	public int weight() {
		return this.weight;
	}
	
	public double initialPenalty() {
		return this.initPenalty;
	}
}
