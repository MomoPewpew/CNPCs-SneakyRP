package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import noppes.npcs.api.item.IItemBook;

public class ItemBookWrapper extends ItemStackWrapper implements IItemBook {
     protected ItemBookWrapper(ItemStack item) {
          super(item);
     }

     public String getTitle() {
          return this.getTag().getString("title");
     }

     public void setTitle(String title) {
          this.getTag().setString("title", title);
     }

     public String getAuthor() {
          return this.getTag().getString("author");
     }

     public void setAuthor(String author) {
          this.getTag().setString("author", author);
     }

     public String[] getText() {
          List list = new ArrayList();
          NBTTagList pages = this.getTag().getTagList("pages", 8);

          for(int i = 0; i < pages.tagCount(); ++i) {
               list.add(pages.func_150307_f(i));
          }

          return (String[])list.toArray(new String[list.size()]);
     }

     public void setText(String[] pages) {
          NBTTagList list = new NBTTagList();
          if (pages != null && pages.length > 0) {
               String[] var3 = pages;
               int var4 = pages.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                    String page = var3[var5];
                    list.appendTag(new NBTTagString(page));
               }
          }

          this.getTag().setTag("pages", list);
     }

     private NBTTagCompound getTag() {
          NBTTagCompound comp = this.item.getTagCompound();
          if (comp == null) {
               this.item.setTagCompound(comp = new NBTTagCompound());
          }

          return comp;
     }

     public boolean isBook() {
          return true;
     }

     public int getType() {
          return 1;
     }
}
