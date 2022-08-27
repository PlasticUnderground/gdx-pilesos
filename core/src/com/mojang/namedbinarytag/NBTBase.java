package com.mojang.namedbinarytag;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase {
	public static final String[] NBTTypes = new String[] { "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]" };
	private String name;
	abstract void write(DataOutput dataoutput) throws IOException;
	abstract void load(DataInput datainput) throws IOException;
	public abstract byte getId();

	protected NBTBase(String par1Str) {
		if (par1Str == null) {
			this.name = "";
		} else {
			this.name = par1Str;
		}
	}
	public NBTBase setName(String par1Str) {
		if (par1Str == null) this.name = "";
		else this.name = par1Str;
		return this;
	}
	
	public String getName() {
		return this.name == null ? "" : this.name;
	}

	public static NBTBase readNamedTag(DataInput par0DataInput) throws IOException {
		byte b0 = par0DataInput.readByte();
		if (b0 == 0) {
			return new NBTTagEnd();
		} else {
			String s = par0DataInput.readUTF();
			NBTBase nbtbase = newTag(b0, s);

			try {
				nbtbase.load(par0DataInput);
				return nbtbase;
			} catch (IOException ioexception) {
				ioexception.printStackTrace();
				throw ioexception;
			}
		}
	}

	public static void writeNamedTag(NBTBase par0NBTBase, DataOutput par1DataOutput) throws IOException {
		par1DataOutput.writeByte(par0NBTBase.getId());
		if (par0NBTBase.getId() != 0) {
			par1DataOutput.writeUTF(par0NBTBase.getName());
			par0NBTBase.write(par1DataOutput);
		}
	}

	public static NBTBase newTag(byte par0, String par1Str) {
		switch (par0) {
		case 0:
			return new NBTTagEnd();
		case 1:
			return new NBTTagByte(par1Str);
		case 2:
			return new NBTTagShort(par1Str);
		case 3:
			return new NBTTagInt(par1Str);
		case 4:
			return new NBTTagLong(par1Str);
		case 5:
			return new NBTTagFloat(par1Str);
		case 6:
			return new NBTTagDouble(par1Str);
		case 7:
			return new NBTTagByteArray(par1Str);
		case 8:
			return new NBTTagString(par1Str);
		case 9:
			return new NBTTagList(par1Str);
		case 10:
			return new NBTTagCompound(par1Str);
		case 11:
			return new NBTTagIntArray(par1Str);
		default:
			return null;
		}
	}
	
	public static String getTagName(byte par0) {
		switch (par0) {
		case 0:
			return "TAG_End";
		case 1:
			return "TAG_Byte";
		case 2:
			return "TAG_Short";
		case 3:
			return "TAG_Int";
		case 4:
			return "TAG_Long";
		case 5:
			return "TAG_Float";
		case 6:
			return "TAG_Double";
		case 7:
			return "TAG_Byte_Array";
		case 8:
			return "TAG_String";
		case 9:
			return "TAG_List";
		case 10:
			return "TAG_Compound";
		case 11:
			return "TAG_Int_Array";
		default:
			return "UNKNOWN";
		}
	}

	public abstract NBTBase copy();

	public boolean equals(Object par1Obj) {
		if (!(par1Obj instanceof NBTBase)) {
			return false;
		} else {
			NBTBase nbtbase = (NBTBase) par1Obj;
			return this.getId() != nbtbase.getId() ? false
					: ((this.name != null || nbtbase.name == null) && (this.name == null || nbtbase.name != null)
							? this.name == null || this.name.equals(nbtbase.name)
							: false);
		}
	}

	public int hashCode() {
		return this.name.hashCode() ^ this.getId();
	}
}
