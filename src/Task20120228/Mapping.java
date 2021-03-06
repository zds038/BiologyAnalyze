package Task20120228;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Mapping {
	
	String base2 = "D:/recover/研究生工作/personal_data/repeatchr1-22/WC/";
	int offset = 50;
	public void readAndoutput(String intFile,String outFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(intFile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine();
			Scanner s = null;
			String seq,reads,cigra;
			int len;
			int start,end;

			while(str != null){
				s = new Scanner(str);
				for(int i=0;i<3;i++)s.next();
				start = s.nextInt();
				s.next();
				cigra = s.next();
				for(int i=0;i<3;i++)s.next();
				reads = s.next();
				len = reads.length();
				end = start+len-1;
				
				seq = RefUtil.refReadsAndFilterRuelst(base2+"W_hg19_Chr1.txt", start, end, RefUtil.POS);
				char[] readsar = reads.toCharArray();
				char[] seqar = seq.toCharArray();
				char[] cig = getChars(cigra, len+offset);
				int of = getOffset(cigra);
				int index = 0;
				
				//reference
				StringBuffer sb1 = new StringBuffer("");
				for(int i=0;i<of;i++)sb1.append("*");
				for(int i=of;i<cig.length;i++){
					if(cig[i]==0)break;
					if(cig[i] == 'I'){
						sb1.append("*");
					}else{
						if(cig[i]=='M' || cig[i]=='D')
							sb1.append(seqar[index++]);
						else
							sb1.append("*");
					}
				}

				//mapping
				StringBuffer sb3 = new StringBuffer("");
				for(int i=0;i<cig.length;i++){
					if(cig[i] == 0) break;
					sb3.append(cig[i]);
				}
				
                //reads
				StringBuffer sb2 = new StringBuffer("");
				index = 0;
				for(int i=0;i<cig.length;i++){
					if(cig[i] == 'D'){
						sb2.append("*");
					}else{
						if(index<readsar.length)
							sb2.append(readsar[index++]);
					}
				}
				bw.write(str);
				bw.newLine();
				bw.write("  ref:"+sb1.toString());
				bw.newLine();
				bw.write("  map:"+sb3.toString());
				bw.newLine();
				bw.write("reads:"+sb2.toString());
				bw.newLine();
				bw.newLine();
				str = br.readLine();
			}
			br.close();
			bw.flush();
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public char[] getChars(String cigra,int readslen){
		int totalD = 0;
	  try{
		char[] c = cigra.toCharArray();
		char[] r = new char[readslen];
		int index = 0;
		int count = 0;
 		for(int i=0;i<c.length;i++){
			if(c[i]>='0'&&c[i]<='9'){
				count = count*10+c[i]-'0';
			}else if(c[i]=='M'){
				for(int j=index;j<index+count;j++)r[j]='M';
				index+=count;
				count=0;
			}else if(c[i]=='D'){
				for(int j=index;j<index+count;j++)r[j]='D';
				index+=count;
				totalD+=count;
				count=0;
			}else if(c[i]=='I'){
				for(int j=index;j<index+count;j++)r[j]='I';
				index+=count;
				totalD+=count;
				count=0;
			}else if(c[i]=='S'){
				for(int j=index;j<index+count;j++)r[j]='S';
				index+=count;
				count=0;
			}else 
				System.out.println("exception !!!!");
		}
		return r;
	  }catch(Exception e){
		  e.printStackTrace();
		  System.out.println(cigra+"\t"+readslen);
		  System.out.println("Total D$$$$$$$$$$$$$$$:"+totalD);
		  return null;
	  }
	}
	public int getOffset(String cigra){
		int total = 0;
		char flag = 0;
		char[] c = cigra.toCharArray();
		for(int i=0;i<c.length;i++){
			if(c[i]>='0'&&c[i]<='9'){
				total = total*10+(c[i]-'0');
			}else{
				flag = c[i];
				break;
			}
		}
		if(flag =='M')return 0;
		else return total;
	}

	public Mapping(){
		readAndoutput("I:/bwa/a.txt", "a.txt");
	}
	
	public static void main(String args[]){
		new Mapping();
	}
}
