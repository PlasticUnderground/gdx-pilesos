package com.mojang.namedbinarytag;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NBTTagCompound extends NBTBase {
	private Map<String, NBTBase> tagMap = new HashMap<String, NBTBase>();
	public NBTTagCompound() {
		super("");
	}

	public NBTTagCompound(String par1Str) {
		super(par1Str);
	}

	void write(DataOutput par1DataOutput) throws IOException {
		Iterator<NBTBase> iterator = this.tagMap.values().iterator();

		while (iterator.hasNext()) {
			NBTBase nbtbase = (NBTBase) iterator.next();
			NBTBase.writeNamedTag(nbtbase, par1DataOutput);
		}

		par1DataOutput.writeByte(0);
	}

	void load(DataInput par1DataInput) throws IOException {
		this.tagMap.clear();
		NBTBase nbtbase;

		while ((nbtbase = NBTBase.readNamedTag(par1DataInput)).getId() != 0) {
			this.tagMap.put(nbtbase.getName(), nbtbase);
		}
	}

	public Collection<NBTBase> getTags() {
		return this.tagMap.values();
	}

	public byte getId() {
		return (byte) 10;
	}

	public void setTag(String par1Str, NBTBase par2NBTBase) {
		this.tagMap.put(par1Str, par2NBTBase.setName(par1Str));
	}

	public void setByte(String par1Str, byte par2) {
		this.tagMap.put(par1Str, new NBTTagByte(par1Str, par2));
	}

	public void setShort(String par1Str, short par2) {
		this.tagMap.put(par1Str, new NBTTagShort(par1Str, par2));
	}

	public void setInteger(String par1Str, int par2) {
		this.tagMap.put(par1Str, new NBTTagInt(par1Str, par2));
	}

	public void setLong(String par1Str, long par2) {
		this.tagMap.put(par1Str, new NBTTagLong(par1Str, par2));
	}

	public void setFloat(String par1Str, float par2) {
		this.tagMap.put(par1Str, new NBTTagFloat(par1Str, par2));
	}

	public void setDouble(String par1Str, double par2) {
		this.tagMap.put(par1Str, new NBTTagDouble(par1Str, par2));
	}

	public void setString(String par1Str, String par2Str) {
		this.tagMap.put(par1Str, new NBTTagString(par1Str, par2Str));
	}

	public void setByteArray(String par1Str, byte[] par2ArrayOfByte) {
		this.tagMap.put(par1Str, new NBTTagByteArray(par1Str, par2ArrayOfByte));
	}

	public void setIntArray(String par1Str, int[] par2ArrayOfInteger) {
		this.tagMap.put(par1Str, new NBTTagIntArray(par1Str, par2ArrayOfInteger));
	}

	public void setCompoundTag(String par1Str, NBTTagCompound par2NBTTagCompound) {
		this.tagMap.put(par1Str, par2NBTTagCompound.setName(par1Str));
	}

	public void setBoolean(String par1Str, boolean par2) {
		this.setByte(par1Str, (byte) (par2 ? 1 : 0));
	}

	public NBTBase getTag(String par1Str) {
		return (NBTBase) this.tagMap.get(par1Str);
	}

	public boolean hasKey(String par1Str) {
		return this.tagMap.containsKey(par1Str);
	}

	public byte getByte(String par1Str) {
		try {
			return !this.tagMap.containsKey(par1Str) ? 0 : ((NBTTagByte) this.tagMap.get(par1Str)).data;
		} catch (ClassCastException classcastexception) {
			throw classcastexception;
		}
	}

	public short getShort(String par1Str) {
		try {
			return !this.tagMap.containsKey(par1Str) ? 0 : ((NBTTagShort) this.tagMap.get(par1Str)).data;
		} catch (ClassCastException classcastexception) {
			throw classcastexception;
		}
	}

	public int getInteger(String par1Str) {
		try {
			return !this.tagMap.containsKey(par1Str) ? 0 : ((NBTTagInt) this.tagMap.get(par1Str)).data;
		} catch (ClassCastException classcastexception) {
			throw classcastexception;
		}
	}

	public long getLong(String par1Str) {
		try {
			return !this.tagMap.containsKey(par1Str) ? 0L : ((NBTTagLong) this.tagMap.get(par1Str)).data;
		} catch (ClassCastException classcastexception) {
			throw classcastexception;
		}
	}

	public float getFloat(String par1Str) {
		try {
			return !this.tagMap.containsKey(par1Str) ? 0.0F : ((NBTTagFloat) this.tagMap.get(par1Str)).data;
		} catch (ClassCastException classcastexception) {
			throw classcastexception;
		}
	}

	public double getDouble(String par1Str) {
		try {
			return !this.tagMap.containsKey(par1Str) ? 0.0D : ((NBTTagDouble) this.tagMap.get(par1Str)).data;
		} catch (ClassCastException classcastexception) {
			throw classcastexception;
		}
	}

	public String getString(String par1Str) {
		try {
			return !this.tagMap.containsKey(par1Str) ? "" : ((NBTTagString) this.tagMap.get(par1Str)).data;
		} catch (ClassCastException classcastexception) {
			throw classcastexception;
		}
	}

	public byte[] getByteArray(String par1Str) {
		try {
			return !this.tagMap.containsKey(par1Str) ? new byte[0]
					: ((NBTTagByteArray) this.tagMap.get(par1Str)).byteArray;
		} catch (ClassCastException classcastexception) {
			throw classcastexception;
		}
	}

	public int[] getIntArray(String par1Str) {
		try {
			return !this.tagMap.containsKey(par1Str) ? new int[0]
					: ((NBTTagIntArray) this.tagMap.get(par1Str)).intArray;
		} catch (ClassCastException classcastexception) {
			throw classcastexception;
		}
	}

	public NBTTagCompound getCompoundTag(String par1Str) {
		try {
			return !this.tagMap.containsKey(par1Str) ? new NBTTagCompound(par1Str)
					: (NBTTagCompound) this.tagMap.get(par1Str);
		} catch (ClassCastException classcastexception) {
			throw classcastexception;
		}
	}

	public NBTTagList getTagList(String par1Str) {
		try {
			return !this.tagMap.containsKey(par1Str) ? new NBTTagList(par1Str) : (NBTTagList) this.tagMap.get(par1Str);
		} catch (ClassCastException classcastexception) {
			throw classcastexception;
		}
	}

	public boolean getBoolean(String par1Str) {
		return this.getByte(par1Str) != 0;
	}

	public void removeTag(String par1Str) {
		this.tagMap.remove(par1Str);
	}

	public String toString() {
		String s = this.getName() + ":[";
		String s1;

		for (Iterator<String> iterator = this.tagMap.keySet().iterator(); iterator
				.hasNext(); s = s + s1 + ":" + this.tagMap.get(s1) + ",") {
			s1 = (String) iterator.next();
		}

		return s + "]";
	}

	public boolean hasNoTags() {
		return this.tagMap.isEmpty();
	}

	public NBTBase copy() {
		NBTTagCompound nbttagcompound = new NBTTagCompound(this.getName());
		Iterator<String> iterator = this.tagMap.keySet().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			nbttagcompound.setTag(s, ((NBTBase) this.tagMap.get(s)).copy());
		}

		return nbttagcompound;
	}

	public boolean equals(Object par1Obj) {
		if (super.equals(par1Obj)) {
			NBTTagCompound nbttagcompound = (NBTTagCompound) par1Obj;
			return this.tagMap.entrySet().equals(nbttagcompound.tagMap.entrySet());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return super.hashCode() ^ this.tagMap.hashCode();
	}

	static Map<String, NBTBase> getTagMap(NBTTagCompound par0NBTTagCompound) {
		return par0NBTTagCompound.tagMap;
	}
}