package noppes.npcs.controllers.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.handler.data.IRecipe;
import noppes.npcs.controllers.RecipeController;

public class RecipeCarpentry extends ShapedRecipes implements IRecipe {
     public int id = -1;
     public String name = "";
     public Availability availability = new Availability();
     public boolean isGlobal = false;
     public boolean ignoreDamage = false;
     public boolean ignoreNBT = false;
     public boolean savesRecipe = true;

     public RecipeCarpentry(int width, int height, NonNullList recipe, ItemStack result) {
          super("customnpcs", width, height, recipe, result);
     }

     public RecipeCarpentry(String name) {
          super("customnpcs", 0, 0, NonNullList.func_191196_a(), ItemStack.field_190927_a);
          this.name = name;
     }

     public static RecipeCarpentry read(NBTTagCompound compound) {
          RecipeCarpentry recipe = new RecipeCarpentry(compound.func_74762_e("Width"), compound.func_74762_e("Height"), NBTTags.getIngredientList(compound.func_150295_c("Materials", 10)), new ItemStack(compound.func_74775_l("Item")));
          recipe.name = compound.func_74779_i("Name");
          recipe.id = compound.func_74762_e("ID");
          recipe.availability.readFromNBT(compound.func_74775_l("Availability"));
          recipe.ignoreDamage = compound.func_74767_n("IgnoreDamage");
          recipe.ignoreNBT = compound.func_74767_n("IgnoreNBT");
          recipe.isGlobal = compound.func_74767_n("Global");
          return recipe;
     }

     public NBTTagCompound writeNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.func_74768_a("ID", this.id);
          compound.func_74768_a("Width", this.field_77576_b);
          compound.func_74768_a("Height", this.field_77577_c);
          if (this.func_77571_b() != null) {
               compound.func_74782_a("Item", this.func_77571_b().func_77955_b(new NBTTagCompound()));
          }

          compound.func_74782_a("Materials", NBTTags.nbtIngredientList(this.field_77574_d));
          compound.func_74782_a("Availability", this.availability.writeToNBT(new NBTTagCompound()));
          compound.func_74778_a("Name", this.name);
          compound.func_74757_a("Global", this.isGlobal);
          compound.func_74757_a("IgnoreDamage", this.ignoreDamage);
          compound.func_74757_a("IgnoreNBT", this.ignoreNBT);
          return compound;
     }

     public static RecipeCarpentry createRecipe(RecipeCarpentry recipe, ItemStack par1ItemStack, Object... par2ArrayOfObj) {
          String var3 = "";
          int var4 = 0;
          int var5 = 0;
          int var6 = 0;
          int var9;
          if (par2ArrayOfObj[var4] instanceof String[]) {
               String[] var7 = (String[])((String[])par2ArrayOfObj[var4++]);
               String[] var8 = var7;
               var9 = var7.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                    String var11 = var8[var10];
                    ++var6;
                    var5 = var11.length();
                    var3 = var3 + var11;
               }
          } else {
               while(par2ArrayOfObj[var4] instanceof String) {
                    String var13 = (String)par2ArrayOfObj[var4++];
                    ++var6;
                    var5 = var13.length();
                    var3 = var3 + var13;
               }
          }

          HashMap var14;
          for(var14 = new HashMap(); var4 < par2ArrayOfObj.length; var4 += 2) {
               Character var16 = (Character)par2ArrayOfObj[var4];
               ItemStack var17 = ItemStack.field_190927_a;
               if (par2ArrayOfObj[var4 + 1] instanceof Item) {
                    var17 = new ItemStack((Item)par2ArrayOfObj[var4 + 1]);
               } else if (par2ArrayOfObj[var4 + 1] instanceof Block) {
                    var17 = new ItemStack((Block)par2ArrayOfObj[var4 + 1], 1, -1);
               } else if (par2ArrayOfObj[var4 + 1] instanceof ItemStack) {
                    var17 = (ItemStack)par2ArrayOfObj[var4 + 1];
               }

               var14.put(var16, var17);
          }

          NonNullList ingredients = NonNullList.func_191196_a();

          for(var9 = 0; var9 < var5 * var6; ++var9) {
               char var18 = var3.charAt(var9);
               if (var14.containsKey(var18)) {
                    ingredients.add(var9, Ingredient.func_193369_a(new ItemStack[]{((ItemStack)var14.get(var18)).func_77946_l()}));
               } else {
                    ingredients.add(var9, Ingredient.field_193370_a);
               }
          }

          RecipeCarpentry newrecipe = new RecipeCarpentry(var5, var6, ingredients, par1ItemStack);
          newrecipe.copy(recipe);
          if (var5 == 4 || var6 == 4) {
               newrecipe.isGlobal = false;
          }

          return newrecipe;
     }

     public boolean func_77569_a(InventoryCrafting inventoryCrafting, World world) {
          for(int i = 0; i <= 4 - this.field_77576_b; ++i) {
               for(int j = 0; j <= 4 - this.field_77577_c; ++j) {
                    if (this.checkMatch(inventoryCrafting, i, j, true)) {
                         return true;
                    }

                    if (this.checkMatch(inventoryCrafting, i, j, false)) {
                         return true;
                    }
               }
          }

          return false;
     }

     public ItemStack func_77572_b(InventoryCrafting var1) {
          return this.func_77571_b().func_190926_b() ? ItemStack.field_190927_a : this.func_77571_b().func_77946_l();
     }

     private boolean checkMatch(InventoryCrafting inventoryCrafting, int par2, int par3, boolean par4) {
          for(int i = 0; i < 4; ++i) {
               for(int j = 0; j < 4; ++j) {
                    int var7 = i - par2;
                    int var8 = j - par3;
                    Ingredient ingredient = Ingredient.field_193370_a;
                    if (var7 >= 0 && var8 >= 0 && var7 < this.field_77576_b && var8 < this.field_77577_c) {
                         if (par4) {
                              ingredient = (Ingredient)this.field_77574_d.get(this.field_77576_b - var7 - 1 + var8 * this.field_77576_b);
                         } else {
                              ingredient = (Ingredient)this.field_77574_d.get(var7 + var8 * this.field_77576_b);
                         }
                    }

                    ItemStack var10 = inventoryCrafting.func_70463_b(i, j);
                    if (!var10.func_190926_b() || ingredient.func_193365_a().length == 0) {
                         return false;
                    }

                    ItemStack var9 = ingredient.func_193365_a()[0];
                    if ((!var10.func_190926_b() || !var9.func_190926_b()) && !NoppesUtilPlayer.compareItems(var9, var10, this.ignoreDamage, this.ignoreNBT)) {
                         return false;
                    }
               }
          }

          return true;
     }

     public NonNullList func_179532_b(InventoryCrafting inventoryCrafting) {
          NonNullList list = NonNullList.func_191197_a(inventoryCrafting.func_70302_i_(), ItemStack.field_190927_a);

          for(int i = 0; i < list.size(); ++i) {
               ItemStack itemstack = inventoryCrafting.func_70301_a(i);
               list.set(i, ForgeHooks.getContainerItem(itemstack));
          }

          return list;
     }

     public void copy(RecipeCarpentry recipe) {
          this.id = recipe.id;
          this.name = recipe.name;
          this.availability = recipe.availability;
          this.isGlobal = recipe.isGlobal;
          this.ignoreDamage = recipe.ignoreDamage;
          this.ignoreNBT = recipe.ignoreNBT;
     }

     public ItemStack getCraftingItem(int i) {
          if (this.field_77574_d != null && i < this.field_77574_d.size()) {
               Ingredient ingredients = (Ingredient)this.field_77574_d.get(i);
               return ingredients.func_193365_a().length == 0 ? ItemStack.field_190927_a : ingredients.func_193365_a()[0];
          } else {
               return ItemStack.field_190927_a;
          }
     }

     public boolean isValid() {
          if (this.field_77574_d.size() != 0 && !this.func_77571_b().func_190926_b()) {
               Iterator var1 = this.field_77574_d.iterator();

               Ingredient ingredient;
               do {
                    if (!var1.hasNext()) {
                         return false;
                    }

                    ingredient = (Ingredient)var1.next();
               } while(ingredient.func_193365_a().length <= 0);

               return true;
          } else {
               return false;
          }
     }

     public String getName() {
          return this.name;
     }

     public ItemStack getResult() {
          return this.func_77571_b();
     }

     public boolean isGlobal() {
          return this.isGlobal;
     }

     public void setIsGlobal(boolean bo) {
          this.isGlobal = bo;
     }

     public boolean getIgnoreNBT() {
          return this.ignoreNBT;
     }

     public void setIgnoreNBT(boolean bo) {
          this.ignoreNBT = bo;
     }

     public boolean getIgnoreDamage() {
          return this.ignoreDamage;
     }

     public void setIgnoreDamage(boolean bo) {
          this.ignoreDamage = bo;
     }

     public void save() {
          try {
               RecipeController.instance.saveRecipe(this);
          } catch (IOException var2) {
          }

     }

     public void delete() {
          RecipeController.instance.delete(this.id);
     }

     public int func_192403_f() {
          return this.field_77576_b;
     }

     public int func_192404_g() {
          return this.field_77577_c;
     }

     public ItemStack[] getRecipe() {
          List list = new ArrayList();
          Iterator var2 = this.field_77574_d.iterator();

          while(var2.hasNext()) {
               Ingredient ingredient = (Ingredient)var2.next();
               if (ingredient.func_193365_a().length > 0) {
                    list.add(ingredient.func_193365_a()[0]);
               }
          }

          return (ItemStack[])list.toArray(new ItemStack[list.size()]);
     }

     public void saves(boolean bo) {
          this.savesRecipe = bo;
     }

     public boolean saves() {
          return this.savesRecipe;
     }

     public int getId() {
          return this.id;
     }
}
