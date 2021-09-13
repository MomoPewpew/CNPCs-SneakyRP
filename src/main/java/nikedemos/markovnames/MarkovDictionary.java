package nikedemos.markovnames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class MarkovDictionary {
     private Random rng;
     private int sequenceLen;
     private HashMap2D occurrences;

     public MarkovDictionary(String dictionary, int seqlen, Random rng) {
          this.sequenceLen = 3;
          this.occurrences = new HashMap2D();
          this.rng = rng;

          try {
               this.applyDictionary(dictionary, seqlen);
          } catch (Exception var5) {
               var5.printStackTrace();
          }

     }

     public MarkovDictionary(String dictionary, int seqlen) {
          this(dictionary, seqlen, new Random());
     }

     public MarkovDictionary(String dictionary) {
          this(dictionary, 3, new Random());
     }

     public MarkovDictionary(String dictionary, Random rng) {
          this(dictionary, 3, rng);
     }

     private InputStream getResource(ResourceLocation resourceLocation) {
          ModContainer container = Loader.instance().activeModContainer();
          if (container != null) {
               String resourcePath = String.format("/%s/%s/%s", "assets", resourceLocation.func_110624_b(), resourceLocation.func_110623_a());
               InputStream resourceAsStream = null;

               try {
                    resourceAsStream = container.getMod().getClass().getResourceAsStream(resourcePath);
               } catch (Exception var6) {
                    var6.printStackTrace();
               }

               if (resourceAsStream != null) {
                    return resourceAsStream;
               } else {
                    throw new RuntimeException("Could not find resource " + resourceLocation);
               }
          } else {
               throw new RuntimeException("Failed to find current mod while looking for resource " + resourceLocation);
          }
     }

     public String getCapitalized(String str) {
          if (str != null && !str.isEmpty()) {
               char[] chars = str.toCharArray();
               chars[0] = Character.toUpperCase(chars[0]);
               return new String(chars);
          } else {
               return str;
          }
     }

     public static String readFile(String path) throws IOException {
          byte[] encoded = Files.readAllBytes(Paths.get(path));
          return new String(encoded, StandardCharsets.UTF_8);
     }

     public void incrementSafe(String str1, String str2) {
          if (this.occurrences.containsKeys(str1, str2)) {
               int curr = (Integer)this.occurrences.get(str1, str2);
               this.occurrences.put(str1, str2, curr + 1);
          } else {
               this.occurrences.put(str1, str2, 1);
          }

     }

     public String generateWord() {
          int allEntries = 0;
          Iterator i = this.occurrences.mMap.entrySet().iterator();

          while(i.hasNext()) {
               Entry pair = (Entry)i.next();
               String k = (String)pair.getKey();
               if (k.startsWith("_[") && k.endsWith("_")) {
                    allEntries += (Integer)this.occurrences.get(k, "_TOTAL_");
               }
          }

          int randomNumber = this.rng.nextInt(allEntries);
          Iterator it = this.occurrences.mMap.entrySet().iterator();
          StringBuilder sequence = new StringBuilder("");

          while(it.hasNext()) {
               Entry pair = (Entry)it.next();
               String k = (String)pair.getKey();
               if (k.startsWith("_[") && k.endsWith("_")) {
                    int topLevelEntries = (Integer)this.occurrences.get(k, "_TOTAL_");
                    if (randomNumber < topLevelEntries) {
                         sequence.append(k.substring(1, this.sequenceLen + 1));
                         break;
                    }

                    randomNumber -= topLevelEntries;
               }
          }

          StringBuilder word = new StringBuilder("");
          word.append(sequence);

          while(sequence.charAt(sequence.length() - 1) != ']') {
               int subSize = 0;

               Entry entry;
               for(Iterator j = ((Map)this.occurrences.mMap.get(sequence.toString())).entrySet().iterator(); j.hasNext(); subSize += (Integer)entry.getValue()) {
                    entry = (Entry)j.next();
               }

               randomNumber = this.rng.nextInt(subSize);
               Iterator k = ((Map)this.occurrences.mMap.get(sequence.toString())).entrySet().iterator();

               String chosen;
               int occu;
               for(chosen = ""; k.hasNext(); randomNumber -= occu) {
                    Entry entry = (Entry)k.next();
                    occu = (Integer)this.occurrences.get(sequence.toString(), entry.getKey());
                    if (randomNumber < occu) {
                         chosen = (String)entry.getKey();
                         break;
                    }
               }

               word.append(chosen);
               sequence.delete(0, 1);
               sequence.append(chosen);
          }

          return this.getPost(word.substring(1, word.length() - 1));
     }

     public String getPost(String str) {
          return this.getCapitalized(str);
     }

     public void applyDictionary(String dictionaryFile, int seqLen) throws IOException {
          StringBuilder input = new StringBuilder();
          ResourceLocation resource = new ResourceLocation("customnpcs:markovnames/" + dictionaryFile);
          BufferedReader readIn = new BufferedReader(new InputStreamReader(this.getResource(resource), "UTF-8"));

          String input_str;
          for(input_str = readIn.readLine(); input_str != null; input_str = readIn.readLine()) {
               input.append(input_str).append(" ");
          }

          readIn.close();
          if (input.length() == 0) {
               throw new RuntimeException("Resource was empty: + " + resource);
          } else {
               if (this.sequenceLen != seqLen) {
                    this.sequenceLen = seqLen;
                    this.occurrences.clear();
               }

               input_str = '[' + input.toString().toLowerCase().replaceAll("[\\t\\n\\r\\s]+", "][") + ']';
               int maxCursorPos = input_str.length() - 1 - this.sequenceLen;

               for(int i = 0; i <= maxCursorPos; ++i) {
                    String seqCurr = input_str.substring(i, i + this.sequenceLen);
                    String seqNext = input_str.substring(i + this.sequenceLen, i + this.sequenceLen + 1);
                    this.incrementSafe(seqCurr, seqNext);
                    StringBuilder meta = (new StringBuilder("_")).append(seqCurr).append("_");
                    this.incrementSafe(meta.toString(), "_TOTAL_");
               }

          }
     }
}
