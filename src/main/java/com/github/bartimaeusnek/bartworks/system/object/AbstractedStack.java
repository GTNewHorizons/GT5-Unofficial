/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.object;

import com.github.bartimaeusnek.bartworks.util.Pair;
import java.io.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class AbstractedStack implements Serializable {

    final Pair<Integer, Short> idDamage;
    final NBTTagCompound mTag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractedStack)) return false;

        AbstractedStack that = (AbstractedStack) o;

        if (this.getIdDamage() != null ? !this.getIdDamage().equals(that.getIdDamage()) : that.getIdDamage() != null)
            return false;
        return this.getmTag() != null ? this.getmTag().equals(that.getmTag()) : that.getmTag() == null;
    }

    @Override
    public int hashCode() {
        int result = this.getIdDamage() != null ? this.getIdDamage().hashCode() : 0;
        result = 31 * result + (this.getmTag() != null ? this.getmTag().hashCode() : 0);
        return result;
    }

    public Pair<Integer, Short> getIdDamage() {
        return this.idDamage;
    }

    public NBTTagCompound getmTag() {
        return this.mTag;
    }

    public AbstractedStack(Pair<Integer, Short> idDamage, NBTTagCompound mTag) {
        this.idDamage = idDamage;
        this.mTag = mTag;
    }

    public AbstractedStack(ItemStack itemStack) {
        if (itemStack == null) throw new UnsupportedOperationException();
        this.idDamage = new Pair<>(Item.getIdFromItem(itemStack.getItem()), (short) itemStack.getItemDamage());
        this.mTag = itemStack.getTagCompound();
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(this);
        return out.toByteArray();
    }

    public AbstractedStack deserialize(byte[] data) throws IOException, ClassNotFoundException, ClassCastException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (AbstractedStack) is.readObject();
    }
}
