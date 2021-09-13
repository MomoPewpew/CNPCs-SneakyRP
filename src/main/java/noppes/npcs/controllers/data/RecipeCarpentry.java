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
          super("customnpcs", 0, 0, NonNullList.create(), ItemStack.EMPTY);
          this.name = name;
     }

     public static RecipeCarpentry read(NBTTagCompound compound) {
          RecipeCarpentry recipe = new RecipeCarpentry(compound.getInteger("Width"), compound.getInteger("Height"), NBTTags.getIngredientList(compound.getTagList("Materials", 10)), new ItemStack(compound.getCompoundTag("Item")));
          recipe.name = compound.getString("Name");
          recipe.id = compound.getInteger("ID");
          recipe.availability.readFromNBT(compound.getCompoundTag("Availability"));
          recipe.ignoreDamage = compound.getBoolean("IgnoreDamage");
          recipe.ignoreNBT = compound.getBoolean("IgnoreNBT");
          recipe.isGlobal = compound.getBoolean("Global");
          return recipe;
     }

     public NBTTagCompound writeNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.setInteger("ID", this.id);
          compound.setInteger("Width", this.field_77576_b);
          compound.setInteger("Height", this.field_77577_c);
          if (this.getResult() != null) {
               compound.setTag("Item", this.getResult().writeToNBT(new NBTTagCompound()));
          }

          compound.setTag("Materials", NBTTags.nbtIngredientList(this.recipeItems));
          compound.setTag("Availability", this.availability.writeToNBT(new NBTTagCompound()));
          compound.setString("Name", this.name);
          compound.setBoolean("Global", this.isGlobal);
          compound.setBoolean("IgnoreDamage", this.ignoreDamage);
          compound.setBoolean("IgnoreNBT", this.ignoreNBT);
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
               ItemStack var17 = ItemStack.EMPTY;
               if (par2ArrayOfObj[var4 + 1] instanceof Item) {
                    var17 = new ItemStack((Item)par2ArrayOfObj[var4 + 1]);
               } else if (par2ArrayOfObj[var4 + 1] instanceof Block) {
                    var17 = new ItemStack((Block)par2ArrayOfObj[var4 + 1], 1, -1);
               } else if (par2ArrayOfObj[var4 + 1] instanceof ItemStack) {
                    var17 = (ItemStack)par2ArrayOfObj[var4 + 1];
               }

               var14.put(var16, var17);
          }

          NonNullList ingredients = NonNullList.create();

          for(var9 = 0; var9 < var5 * var6; ++var9) {
               char var18 = var3.charAt(var9);
               if (var14.containsKey(var18)) {
                    ingredients.add(var9, Ingredient.fromStacks(new ItemStack[]{((ItemStack)var14.get(var18)).copy()}));
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
          return this.getResult().isEmpty() ? ItemStack.EMPTY : this.getResult().copy();
     }

     private boolean checkMatch(InventoryCrafting inventoryCrafting, int par2, int par3, boolean par4) {
          for(int i = 0; i < 4; ++i) {
               for(int j = 0; j < 4; ++j) {
                    int var7 = i - par2;
                    int var8 = j - par3;
                    Ingredient ingredient = Ingredient.field_193370_a;
                    if (var7 >= 0 && var8 >= 0 && var7 < this.field_77576_b && var8 < this.field_77577_c) {
                         if (par4) {
                              ingredient = (Ingredient)this.recipeItems.get(this.field_77576_b - var7 - 1 + var8 * this.field_77576_b);
                         } else {
                              ingredient = (Ingredient)this.recipeItems.get(var7 + var8 * this.field_77576_b);
                         }
                    }

                    ItemStack var10 = inventoryCrafting.getStackInRowAndColumn(i, j);
                    if (!var10.isEmpty() || ingredient.getMatchingStacks().length == 0) {
                         return false;
                    }

                    ItemStack var9 = ingredient.getMatchingStacks()[0];
                    if ((!var10.isEmpty() || !var9.isEmpty()) && !NoppesUtilPlayer.compareItems(var9, var10, this.ignoreDamage, this.ignoreNBT)) {
                         return false;
                    }
               }
          }

          return true;
     }

     public NonNullList func_179532_b(InventoryCrafting inventoryCrafting) {
          NonNullList list = NonNullList.withSize(inventoryCrafting.getSizeInventory(), ItemStack.EMPTY);

          for(int i = 0; i < list.size(); ++i) {
               ItemStack itemstack = inventoryCrafting.getStackInSlot(i);
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
          if (this.recipeItems != null && i < this.recipeItems.size()) {
               Ingredient ingredients = (Ingredient)this.recipeItems.get(i);
               return ingredients.getMatchingStacks().length == 0 ? ItemStack.EMPTY : ingredients.getMatchingStacks()[0];
          } else {
               return ItemStack.EMPTY;
          }
     }

     public boolean isValid() {
          if (this.recipeItems.size() != 0 && !this.getResult().isEmpty()) {
               Iterator var1 = this.recipeItems.iterator();

               Ingredient ingredient;
               do {
                    if (!var1.hasNext()) {
                         return false;
                    }

                    ingredient = (Ingredient)var1.next();
               } while(ingredient.getMatchingStacks().length <= 0);

               return true;
          } else {
               return false;
          }
     }

     public String getName() {
          return this.name;
     }

     public ItemStack getResult() {
          return this.getResult();
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
          Iterator var2 = this.recipeItems.iterator();

          while(var2.hasNext()) {
               Ingredient ingredient = (Ingredient)var2.next();
               if (ingredient.getMatchingStacks().length > 0) {
                    list.add(ingredient.getMatchingStacks()[0]);
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
