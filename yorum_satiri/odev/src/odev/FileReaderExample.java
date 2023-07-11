/**
* @author Beyza Selin ÇİLLİ
*/

package odev;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;


public class FileReaderExample {
    public static void main(String[] args) {
        if (args.length < 1) {                 //args.length'in 1'den küçük olduğunu kontrol eder.
            System.out.println("Kullanim: java -jar <jar-file> <dosyaAdi>");  //"Kullanim: java -jar <jar-file> <dosyaAdi>" mesajı yazdırılır.
            System.exit(1);    // programı hemen sonlandırır ve 1 değerini geri döndürür.
        }

        String dosyaAdi = args[0];  // args dizisinin ilk öğesini (args[0]) dosyaAdi adlı bir String değişkene atar. 
        String sinifAdi = "";          //sinifAdi adlı boş String değişkeni tanımlanır.
        String dosyaIceriigi = ""; //dosyaIceriigi boş String değişkeni tanımlanır.
       

        try (BufferedReader dosya_oku = new BufferedReader(new FileReader(dosyaAdi))) {     //'try' bloğu kullanılarak, dosya kaynaklarının otomatik olarak serbest bırakılması sağlanır.
            String satir;                            //satır adında string değişkeni tanımlanır
            while ((satir = dosya_oku.readLine()) != null) {  //while döngüsü, br.readLine() yöntemi null olmadığı sürece dosyanın her satırını okur. 
            	dosyaIceriigi+= satir+ "\n";    //satir adlı bir String değişkenine atanır ve dosyaIceriigi değişkenine eklenir.
                if (satir.matches("^\\s*public\\s+class\\s+\\w+.*")) {  //satir değişkeninin, Java sınıfı tanımı ile eşleşip eşleşmediğini kontrol eder
                    Pattern sinifPattern = Pattern.compile("^\\s*public\\s+class\\s+(\\w+).*");  // sinifPattern adlı Pattern nesnesi oluşturulur. 
                    Matcher sinifMatcher = sinifPattern.matcher(satir);   //sinifMatcher adlı Matcher nesnesi oluşturulur.
                    if (sinifMatcher.matches()) {  //Eğer düzenli ifade ile string eşleşiyorsa
                    	sinifAdi = sinifMatcher.group(1);  // sinifMatcher.group(1) ifadesi, yakalanan eşleşmenin ilk grup ifadesini (parantez içindeki ifade) sinifAdi adlı değişkene atar.
                    }
                }
            }
        } 
        
        catch (IOException okuma_hata_mesaj) {  //Eğer dosya okunurken bir hata oluşursa
            System.err.println("Dosyayi okurken hata olustu... " + okuma_hata_mesaj.getMessage());   // hatanın mesajını ekrana yazdırır
            System.exit(1);  //programı sonlandırır
        }

        System.out.println("Sinif: " + sinifAdi + "\n");  //sınıf adı SinifAdi nesnesi ile ekrana yazdırılır.
        
        Pattern fonksiyonPattern = Pattern.compile("(\\/\\*\\*.*?\\*\\/)?\\s*(public|private|protected)?\\s+(static\\s+)?(final\\s+)?(synchronized\\s+)?\\w+\\s+\\w+\\s*\\(.*?\\)\\s*(throws\\s+\\w+(\\s*,\\s*\\w+)*)?\\s*\\{.*?\\}", Pattern.DOTALL);
     // fonksiyonPattern adlı Pattern nesnesi oluşturulur. 
        Matcher fonksiyonMatcher = fonksiyonPattern.matcher(dosyaIceriigi);   // fonksiyonMatcher adlı Matcher nesnesi oluşturulur. 

        int tekliSatirYorumuSayaci = 0;   //tekli satır yorumlarının sayacı için integer değerinde tekliSatirYorumuSayaci tanımlanır. 
        int cokluSatirYorumuSayaci = 0;  //çoklu satır yorumlarının sayacı için integer değerinde cokluSatirYorumuSayaci tanımlanır. 
        int javadocYorumuSayaci = 0;    //javadoc satır yorumlarının sayacı için integer değerinde javadocYorumuSayaci tanımlanır. 
        
    // 'try' bloğu kullanılarak, dosya kaynaklarının otomatik olarak serbest bırakılması sağlanır.
        try ( BufferedWriter tekliSatirYorumuTXT = new BufferedWriter(new FileWriter("teksatir.txt"));    // 'teksatir.txt' dosyası için bir BufferedWriter nesnesi oluşturulur.
        	  BufferedWriter cokluSatirYorumuTXT = new BufferedWriter(new FileWriter("coksatir.txt"));   // 'coksatir.txt' dosyası için bir BufferedWriter nesnesi oluşturulur.
        	  BufferedWriter javadocYorumTXT = new BufferedWriter(new FileWriter("javadoc.txt"));       // 'javadoc.txt' dosyası için bir BufferedWriter nesnesi oluşturulur.
            ) 
        
        {
            while (fonksiyonMatcher.find()) {   //tanımlanan desenin eşleştiği sonraki alt dizeye ilerler ve eşleşme bulunamazsa false değeri döndürür.Artık bulunamayana kadar döngünün her turunda tekrarlanır.
                String fonksiyonİsim = "";  //String değerinde fonksiyonİsim değişkeni tanımlanır
                Pattern isimPattern = Pattern.compile("\\s+(\\w+)\\s*\\(.*?\\)\\s*\\{", Pattern.DOTALL);//isimPattern adlı Pattern nesnesi tanımlanır.
                Matcher isimMatcher = isimPattern.matcher(fonksiyonMatcher.group()); //isimMatcher isimli Matcher nesnesi tanımlanır.
              
                if (isimMatcher.find()) {  //desenin sonraki eşleşme bulunduğunda ilgili alt dizeye ilerler.
                	fonksiyonİsim = isimMatcher.group(1);  //, desendeki belirli bir desen parçasını ayıklar veya işler ve bunu fonksiyonİsim değişkenine atar
                }
                
                System.out.println("\tFonksiyon: " + fonksiyonİsim); //fonksiyonin ismi ekrana yazdırılır

                Pattern tekliSatirYorumPattern = Pattern.compile("(?s)(?<=\\{|;)(\\s*\\/\\/[^\\n]*)(?=\\n|$)"); //tekliSatirYorumPattern adlı Pattern nesnesi tanımlanır.
                Matcher tekSatirYorumMatcher = tekliSatirYorumPattern .matcher(fonksiyonMatcher.group()); //tekSatirYorumMatcher isimli Matcher nesnesi tanımlanır.
                
                while (tekSatirYorumMatcher.find()) {   //tanımlanan desenin eşleştiği sonraki alt dizeye ilerler ve eşleşme bulunamazsa false değeri döndürür.Artık bulunamayana kadar döngünün her turunda tekrarlanır.
                	tekliSatirYorumuSayaci++; //tekliSatirYorumuSayaci değerini 1 arttırır.
                    String yorumlar = tekSatirYorumMatcher.group(); //, desendeki belirli bir desen parçasını ayıklar veya işler ve bunu yorumlar değişkenine atar
                    tekliSatirYorumuTXT.write("Fonksiyon: " + fonksiyonİsim ); // tekliSatirYorumuTXT dosyası, yazdırılacak dosyanın bir örneği olarak tanımlanır.
                    tekliSatirYorumuTXT.write(yorumlar + "\n" + "\n"+"-----------------------------------" + "\n" + "\n"); //.write() metodu, belirtilen yazı dizisini dosyaya yazmak için kullanılır.
                }

                Pattern cokluSatirYorumPattern = Pattern.compile("(?s)(\\/\\*)(?!\\*)(.|\\n)*?\\*\\/");
                Matcher cokluSatirYorumMatcher = cokluSatirYorumPattern.matcher(fonksiyonMatcher.group());
               
                while (cokluSatirYorumMatcher.find()) {   //tanımlanan desenin eşleştiği sonraki alt dizeye ilerler ve eşleşme bulunamazsa false değeri döndürür.Artık bulunamayana kadar döngünün her turunda tekrarlanır.
                	cokluSatirYorumuSayaci ++;  //cokluSatirYorumuSayaci 1 arttırılır.
                    String yorumlar = cokluSatirYorumMatcher.group();  //, desendeki belirli bir desen parçasını ayıklar veya işler ve bunu yorumlar değişkenine atar
                    cokluSatirYorumuTXT.write("Fonksiyon: " + fonksiyonİsim + "\n"); // cokluSatirYorumuTXT dosyası, yazdırılacak dosyanın bir örneği olarak tanımlanır.
                    cokluSatirYorumuTXT.write(yorumlar + "\n" +"-----------------------------------" + "\n"+ "\n" ); //.write() metodu, belirtilen yazı dizisini dosyaya yazmak için kullanılır.
                }

                Pattern javadocYorumPattern = Pattern.compile("\\/\\*\\*.*?\\*\\/", Pattern.DOTALL);
                Matcher javadocYorumMatcher = javadocYorumPattern.matcher(fonksiyonMatcher.group());
              
                while (javadocYorumMatcher.find()) {  //tanımlanan desenin eşleştiği sonraki alt dizeye ilerler ve eşleşme bulunamazsa false değeri döndürür.Artık bulunamayana kadar döngünün her turunda tekrarlanır.
                	javadocYorumuSayaci++; //javadocYorumuSayaci 1 arttırılır.
                    String yorumlar = javadocYorumMatcher.group();  //desendeki belirli bir desen parçasını ayıklar veya işler ve bunu yorumlar değişkenine atar
                    javadocYorumTXT.write("Fonksiyon: " + fonksiyonİsim + "\n"); // javadocYorumTXT dosyası, yazdırılacak dosyanın bir örneği olarak tanımlanır.
                    javadocYorumTXT.write(yorumlar + "\n" +"-----------------------------------"  + "\n"+ "\n"); //.write() metodu, belirtilen yazı dizisini dosyaya yazmak için kullanılır.
                }
                System.out.println("\n\t\tTek Satir Yorum Sayisi : " + tekliSatirYorumuSayaci);  //tekliSatirYorumuSayaciekrana yazdırılır.
                System.out.println("\t\tÇok Satir Yorum Sayisi : " + cokluSatirYorumuSayaci );  //cokluSatirYorumuSayaci ekrana yazdırılır.
                System.out.println("\t\tJavadoc Yorum Sayisi: " +"\t"+ " "+javadocYorumuSayaci);  //javadocYorumuSayaci ekrana yazdırılır.
                System.out.println("-------------------------------------------");
              
                tekliSatirYorumuSayaci=0; //tekliSatirYorumuSayaci her fonksiyon için dongüye girmesi için sıfıra eşitlenir.
                cokluSatirYorumuSayaci =0; //cokluSatirYorumuSayaci her fonksiyon için dongüye girmesi için sıfıra eşitlenir.
                javadocYorumuSayaci=0; //javadocYorumuSayaci her fonksiyon için dongüye girmesi için sıfıra eşitlenir.
            } 
           
    }
    catch (IOException dosya_hata_mesaj) {  //Eğer cikti dosyalari yazilirken hata olustuysa
        System.err.println("Cikti dosyalari yazilirken hata olustu:" + dosya_hata_mesaj.getMessage()); //ekrana hata mesajını yazdır.
        System.exit(1); // programı hemen sonlandırır ve 1 değerini geri döndürür.
    }
}
}