package noppes.npcs.client.gui.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiButtonNextPage;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiClose;
import noppes.npcs.client.gui.util.IGuiError;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiMailmanWrite extends GuiContainerNPCInterface implements ITextfieldListener, IGuiError, IGuiClose, GuiYesNoCallback {
     private static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");
     private static final ResourceLocation bookWidgets = new ResourceLocation("textures/gui/widgets.png");
     private static final ResourceLocation bookInventory = new ResourceLocation("textures/gui/container/inventory.png");
     private int updateCount;
     private int bookImageWidth = 192;
     private int bookImageHeight = 192;
     private int bookTotalPages = 1;
     private int currPage;
     private NBTTagList bookPages;
     private GuiButtonNextPage buttonNextPage;
     private GuiButtonNextPage buttonPreviousPage;
     private boolean canEdit;
     private boolean canSend;
     private boolean hasSend = false;
     public static GuiScreen parent;
     public static PlayerMail mail = new PlayerMail();
     private Minecraft mc = Minecraft.func_71410_x();
     private String username = "";
     private GuiNpcLabel error;

     public GuiMailmanWrite(ContainerMail container, boolean canEdit, boolean canSend) {
          super((EntityNPCInterface)null, container);
          this.title = "";
          this.canEdit = canEdit;
          this.canSend = canSend;
          if (mail.message.func_74764_b("pages")) {
               this.bookPages = mail.message.func_150295_c("pages", 8);
          }

          if (this.bookPages != null) {
               this.bookPages = this.bookPages.func_74737_b();
               this.bookTotalPages = this.bookPages.func_74745_c();
               if (this.bookTotalPages < 1) {
                    this.bookTotalPages = 1;
               }
          } else {
               this.bookPages = new NBTTagList();
               this.bookPages.func_74742_a(new NBTTagString(""));
               this.bookTotalPages = 1;
          }

          this.field_146999_f = 360;
          this.field_147000_g = 260;
          this.drawDefaultBackground = false;
          this.closeOnEsc = true;
     }

     public void func_73876_c() {
          super.func_73876_c();
          ++this.updateCount;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.field_146292_n.clear();
          Keyboard.enableRepeatEvents(true);
          if (this.canEdit && !this.canSend) {
               this.addLabel(new GuiNpcLabel(0, "mailbox.sender", this.field_147003_i + 170, this.field_147009_r + 32, 0));
          } else {
               this.addLabel(new GuiNpcLabel(0, "mailbox.username", this.field_147003_i + 170, this.field_147009_r + 32, 0));
          }

          if (this.canEdit && !this.canSend) {
               this.addTextField(new GuiNpcTextField(2, this, this.field_146289_q, this.field_147003_i + 170, this.field_147009_r + 42, 114, 20, mail.sender));
          } else if (this.canEdit) {
               this.addTextField(new GuiNpcTextField(0, this, this.field_146289_q, this.field_147003_i + 170, this.field_147009_r + 42, 114, 20, this.username));
          } else {
               this.addLabel(new GuiNpcLabel(10, mail.sender, this.field_147003_i + 170, this.field_147009_r + 42, 0));
          }

          this.addLabel(new GuiNpcLabel(1, "mailbox.subject", this.field_147003_i + 170, this.field_147009_r + 72, 0));
          if (this.canEdit) {
               this.addTextField(new GuiNpcTextField(1, this, this.field_146289_q, this.field_147003_i + 170, this.field_147009_r + 82, 114, 20, mail.subject));
          } else {
               this.addLabel(new GuiNpcLabel(11, mail.subject, this.field_147003_i + 170, this.field_147009_r + 82, 0));
          }

          this.addLabel(this.error = new GuiNpcLabel(2, "", this.field_147003_i + 170, this.field_147009_r + 114, 16711680));
          if (this.canEdit && !this.canSend) {
               this.addButton(new GuiNpcButton(0, this.field_147003_i + 200, this.field_147009_r + 171, 60, 20, "gui.done"));
          } else if (this.canEdit) {
               this.addButton(new GuiNpcButton(0, this.field_147003_i + 200, this.field_147009_r + 171, 60, 20, "mailbox.send"));
          }

          if (!this.canEdit && !this.canSend) {
               this.addButton(new GuiNpcButton(4, this.field_147003_i + 200, this.field_147009_r + 171, 60, 20, "selectWorld.deleteButton"));
          }

          if (!this.canEdit || this.canSend) {
               this.addButton(new GuiNpcButton(3, this.field_147003_i + 200, this.field_147009_r + 194, 60, 20, "gui.cancel"));
          }

          this.field_146292_n.add(this.buttonNextPage = new GuiButtonNextPage(1, this.field_147003_i + 120, this.field_147009_r + 156, true));
          this.field_146292_n.add(this.buttonPreviousPage = new GuiButtonNextPage(2, this.field_147003_i + 38, this.field_147009_r + 156, false));
          this.updateButtons();
     }

     public void func_146281_b() {
          Keyboard.enableRepeatEvents(false);
     }

     private void updateButtons() {
          this.buttonNextPage.setVisible(this.currPage < this.bookTotalPages - 1 || this.canEdit);
          this.buttonPreviousPage.setVisible(this.currPage > 0);
     }

     public void func_73878_a(boolean flag, int i) {
          if (flag) {
               NoppesUtilPlayer.sendData(EnumPlayerPacket.MailDelete, mail.time, mail.sender);
               this.close();
          } else {
               NoppesUtil.openGUI(this.player, this);
          }

     }

     protected void func_146284_a(GuiButton par1GuiButton) {
          if (par1GuiButton.enabled) {
               int id = par1GuiButton.id;
               if (id == 0) {
                    mail.message.func_74782_a("pages", this.bookPages);
                    if (this.canSend) {
                         if (!this.hasSend) {
                              this.hasSend = true;
                              NoppesUtilPlayer.sendData(EnumPlayerPacket.MailSend, this.username, mail.writeNBT());
                         }
                    } else {
                         this.close();
                    }
               }

               if (id == 3) {
                    this.close();
               }

               if (id == 4) {
                    GuiYesNo guiyesno = new GuiYesNo(this, "", I18n.func_74838_a("gui.deleteMessage"), 0);
                    this.displayGuiScreen(guiyesno);
               } else if (id == 1) {
                    if (this.currPage < this.bookTotalPages - 1) {
                         ++this.currPage;
                    } else if (this.canEdit) {
                         this.addNewPage();
                         if (this.currPage < this.bookTotalPages - 1) {
                              ++this.currPage;
                         }
                    }
               } else if (id == 2 && this.currPage > 0) {
                    --this.currPage;
               }

               this.updateButtons();
          }

     }

     private void addNewPage() {
          if (this.bookPages != null && this.bookPages.func_74745_c() < 50) {
               this.bookPages.func_74742_a(new NBTTagString(""));
               ++this.bookTotalPages;
          }

     }

     public void func_73869_a(char par1, int par2) {
          if (!GuiNpcTextField.isActive() && this.canEdit) {
               this.keyTypedInBook(par1, par2);
          } else {
               super.func_73869_a(par1, par2);
          }

     }

     private void keyTypedInBook(char par1, int par2) {
          switch(par1) {
          case '\u0016':
               this.func_74160_b(GuiScreen.func_146277_j());
               return;
          default:
               switch(par2) {
               case 14:
                    String s = this.func_74158_i();
                    if (s.length() > 0) {
                         this.func_74159_a(s.substring(0, s.length() - 1));
                    }

                    return;
               case 28:
               case 156:
                    this.func_74160_b("\n");
                    return;
               default:
                    if (ChatAllowedCharacters.func_71566_a(par1)) {
                         this.func_74160_b(Character.toString(par1));
                    }

               }
          }
     }

     private String func_74158_i() {
          return this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.func_74745_c() ? this.bookPages.func_150307_f(this.currPage) : "";
     }

     private void func_74159_a(String par1Str) {
          if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.func_74745_c()) {
               this.bookPages.func_150304_a(this.currPage, new NBTTagString(par1Str));
          }

     }

     private void func_74160_b(String par1Str) {
          String s1 = this.func_74158_i();
          String s2 = s1 + par1Str;
          int i = this.mc.fontRenderer.func_78267_b(s2 + "" + TextFormatting.BLACK + "_", 118);
          if (i <= 118 && s2.length() < 256) {
               this.func_74159_a(s2);
          }

     }

     public void func_73863_a(int par1, int par2, float par3) {
          this.func_146270_b(0);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.mc.func_110434_K().bindTexture(bookGuiTextures);
          this.drawTexturedModalRect(this.field_147003_i + 130, this.field_147009_r + 22, 0, 0, this.bookImageWidth, this.bookImageHeight / 3);
          this.drawTexturedModalRect(this.field_147003_i + 130, this.field_147009_r + 22 + this.bookImageHeight / 3, 0, this.bookImageHeight / 2, this.bookImageWidth, this.bookImageHeight / 2);
          this.drawTexturedModalRect(this.field_147003_i, this.field_147009_r + 2, 0, 0, this.bookImageWidth, this.bookImageHeight);
          this.mc.func_110434_K().bindTexture(bookInventory);
          this.drawTexturedModalRect(this.field_147003_i + 20, this.field_147009_r + 173, 0, 82, 180, 55);
          this.drawTexturedModalRect(this.field_147003_i + 20, this.field_147009_r + 228, 0, 140, 180, 28);
          String s = net.minecraft.client.resources.I18n.func_135052_a("book.pageIndicator", new Object[]{this.currPage + 1, this.bookTotalPages});
          String s1 = "";
          if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.func_74745_c()) {
               s1 = this.bookPages.func_150307_f(this.currPage);
          }

          if (this.canEdit) {
               if (this.mc.fontRenderer.func_78260_a()) {
                    s1 = s1 + "_";
               } else if (this.updateCount / 6 % 2 == 0) {
                    s1 = s1 + "" + TextFormatting.BLACK + "_";
               } else {
                    s1 = s1 + "" + TextFormatting.GRAY + "_";
               }
          }

          int l = this.mc.fontRenderer.func_78256_a(s);
          this.mc.fontRenderer.func_78276_b(s, this.field_147003_i - l + this.bookImageWidth - 44, this.field_147009_r + 18, 0);
          this.mc.fontRenderer.func_78279_b(s1, this.field_147003_i + 36, this.field_147009_r + 18 + 16, 116, 0);
          this.func_73733_a(this.field_147003_i + 175, this.field_147009_r + 136, this.field_147003_i + 269, this.field_147009_r + 154, -1072689136, -804253680);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.mc.func_110434_K().bindTexture(bookWidgets);

          for(int i = 0; i < 4; ++i) {
               this.drawTexturedModalRect(this.field_147003_i + 175 + i * 24, this.field_147009_r + 134, 0, 22, 24, 24);
          }

          super.func_73863_a(par1, par2, par3);
     }

     public void close() {
          this.mc.displayGuiScreen(parent);
          parent = null;
          mail = new PlayerMail();
     }

     public void unFocused(GuiNpcTextField textField) {
          if (textField.field_175208_g == 0) {
               this.username = textField.func_146179_b();
          }

          if (textField.field_175208_g == 1) {
               mail.subject = textField.func_146179_b();
          }

          if (textField.field_175208_g == 2) {
               mail.sender = textField.func_146179_b();
          }

     }

     public void setError(int i, NBTTagCompound data) {
          if (i == 0) {
               this.error.label = I18n.func_74838_a("mailbox.errorUsername");
          }

          if (i == 1) {
               this.error.label = I18n.func_74838_a("mailbox.errorSubject");
          }

          this.hasSend = false;
     }

     public void setClose(int i, NBTTagCompound data) {
          this.player.func_145747_a(new TextComponentTranslation("mailbox.succes", new Object[]{data.func_74779_i("username")}));
     }

     public void save() {
     }
}
