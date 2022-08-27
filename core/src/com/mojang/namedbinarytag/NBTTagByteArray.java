package com.mojang.namedbinarytag;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagByteArray extends NBTBase {
	public byte[] byteArray;

	public NBTTagByteArray(String par1Str) {
		super(par1Str);
	}

	public NBTTagByteArray(String par1Str, byte[] par2ArrayOfByte) {
		super(par1Str);
		this.byteArray = par2ArrayOfByte;
	}

	void write(DataOutput par1DataOutput) throws IOException {
		par1DataOutput.writeInt(this.byteArray.length);
		par1DataOutput.write(this.byteArray);
	}

	void load(DataInput par1DataInput) throws IOException {
		int i = par1DataInput.readInt();
		this.byteArray = new byte[i];
		par1DataInput.readFully(this.byteArray);
	}

	public byte getId() {
		return (byte) 7;
	}

	public String toString() {
		return "[" + this.byteArray.length + " bytes]";
	}

	public NBTBase copy() {
		byte[] abyte = new byte[this.byteArray.length];
		System.arraycopy(this.byteArray, 0, abyte, 0, this.byteArray.length);
		return new NBTTagByteArray(this.getName(), abyte);
	}

	public boolean equals(Object par1Obj) {
		return super.equals(par1Obj) ? Arrays.equals(this.byteArray, ((NBTTagByteArray) par1Obj).byteArray) : false;
	}

	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(this.byteArray);
	}
}
