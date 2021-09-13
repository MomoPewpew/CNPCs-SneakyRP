package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.EventHooks;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.ITimers;
import noppes.npcs.controllers.IScriptBlockHandler;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityNPCInterface;

public class DataTimers implements ITimers {
     private Object parent;
     private Map timers = new HashMap();

     public DataTimers(Object parent) {
          this.parent = parent;
     }

     public void start(int id, int ticks, boolean repeat) {
          if (this.timers.containsKey(id)) {
               throw new CustomNPCsException("There is already a timer with id: " + id, new Object[0]);
          } else {
               this.timers.put(id, new DataTimers.Timer(id, ticks, repeat));
          }
     }

     public void forceStart(int id, int ticks, boolean repeat) {
          this.timers.put(id, new DataTimers.Timer(id, ticks, repeat));
     }

     public boolean has(int id) {
          return this.timers.containsKey(id);
     }

     public boolean stop(int id) {
          return this.timers.remove(id) != null;
     }

     public void reset(int id) {
          DataTimers.Timer timer = (DataTimers.Timer)this.timers.get(id);
          if (timer == null) {
               throw new CustomNPCsException("There is no timer with id: " + id, new Object[0]);
          } else {
               timer.ticks = 0;
          }
     }

     public void writeToNBT(NBTTagCompound compound) {
          NBTTagList list = new NBTTagList();
          Iterator var3 = this.timers.values().iterator();

          while(var3.hasNext()) {
               DataTimers.Timer timer = (DataTimers.Timer)var3.next();
               NBTTagCompound c = new NBTTagCompound();
               c.func_74768_a("ID", timer.id);
               c.func_74768_a("TimerTicks", timer.id);
               c.func_74757_a("Repeat", timer.repeat);
               c.func_74768_a("Ticks", timer.ticks);
               list.func_74742_a(c);
          }

          compound.func_74782_a("NpcsTimers", list);
     }

     public void readFromNBT(NBTTagCompound compound) {
          Map timers = new HashMap();
          NBTTagList list = compound.func_150295_c("NpcsTimers", 10);

          for(int i = 0; i < list.func_74745_c(); ++i) {
               NBTTagCompound c = list.func_150305_b(i);
               DataTimers.Timer t = new DataTimers.Timer(c.func_74762_e("ID"), c.func_74762_e("TimerTicks"), c.func_74767_n("Repeat"));
               t.ticks = c.func_74762_e("Ticks");
               timers.put(t.id, t);
          }

          this.timers = timers;
     }

     public void update() {
          Iterator var1 = (new ArrayList(this.timers.values())).iterator();

          while(var1.hasNext()) {
               DataTimers.Timer timer = (DataTimers.Timer)var1.next();
               timer.update();
          }

     }

     public void clear() {
          this.timers = new HashMap();
     }

     class Timer {
          public int id;
          private boolean repeat;
          private int timerTicks;
          private int ticks = 0;

          public Timer(int id, int ticks, boolean repeat) {
               this.id = id;
               this.repeat = repeat;
               this.timerTicks = ticks;
               this.ticks = ticks;
          }

          public void update() {
               if (this.ticks-- <= 0) {
                    if (this.repeat) {
                         this.ticks = this.timerTicks;
                    } else {
                         DataTimers.this.stop(this.id);
                    }

                    Object ob = DataTimers.this.parent;
                    if (ob instanceof EntityNPCInterface) {
                         EventHooks.onNPCTimer((EntityNPCInterface)ob, this.id);
                    } else if (ob instanceof PlayerData) {
                         EventHooks.onPlayerTimer((PlayerData)ob, this.id);
                    } else {
                         EventHooks.onScriptBlockTimer((IScriptBlockHandler)ob, this.id);
                    }

               }
          }
     }
}
