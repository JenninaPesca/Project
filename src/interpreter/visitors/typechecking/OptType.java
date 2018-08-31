package interpreter.visitors.typechecking;

import static java.util.Objects.requireNonNull;

public class OptType implements Type{
	private final Type elemType;
	public static final String TYPE_NAME = "OPT";

	public OptType(Type elemType) {
		this.elemType = requireNonNull(elemType);
	}

	public Type getElemType() {
		return elemType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof OptType))
			return false;
		OptType ot = (OptType) obj;
		return elemType.equals(ot.elemType);
	}

	/*@Override
	public int hashCode() {
		return 31 * elemType.hashCode();
	}*/

	@Override
	public String toString() {
		return elemType + " " + TYPE_NAME;
	}
}

