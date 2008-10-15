import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
//import java.math.BigInteger;  不会用，怎么用呢？
import java.util.Date;
//为什么把插入放前面就会出现堆栈溢出错误，而放快速后面确没有这个错误呢

public class CompareSort_Lh {

	/**
	 * @param args
	 */
	public static int n;
	
	public static void insertS_Lh(int[] A){
		int j;
		for(int i=1;i<A.length;i++)
		{
			int key = A[i];
			for(j=i;j>0&&key<A[j-1];j--)
				A[j]=A[j-1];
			A[j]=key;
			
		}
	}//insertS_Lh Finished
	
	public static void quickS_Lh(int[] A, int p, int r){
		if(p<r){
		    int q = Partion(A,p ,r);
			quickS_Lh(A,p, q-1);
			quickS_Lh(A, q+1,r);
		}
	}//quickS_Lh Finished
	protected static int  Partion(int[] A,int p ,int r){
		int key = A[r];
		int i = p-1;
		int	temp;
		for(int j=p;j<r;j++){
			if(key>=A[j]){
				i++;
				temp = A[j];
				A[j] = A[i];
				A[i] = temp;
			}
		}//for Finished
		temp = A[r];
		A[r] = A[i+1];
		A[i+1] = temp;
		return i+1;
	}
	public static void mergeS_Lh(int[] A, int p, int r){
		if(p<r){
			int q = (p+r)/2;
			mergeS_Lh(A,p,q);
			mergeS_Lh(A,q+1,r);
			Merge(A,p,q,r);
		}
	}//mergeS_Lh Finished
	protected static void  Merge(int[] A,int p ,int q,int r){
		int i=p, j=q, k=p;
		int [] B = new int[n];
		while(i<=q&&j<=q)
			if(A[i]<=A[j]){
				B[k]=A[i]; i++; k++;}
			else{B[k]=A[j]; j++; k++;}
		if(i<=q)  for(int n1=k,n2=i;n1<=r&&n2<=q;n1++,n2++) B[n1]=A[n2];
		
		else      for(int n1=k,n2=j;n1<=r&&n2<=r;n1++,n2++) B[n1]=A[n2];
	}//Merge Finished
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(" Please input the NUMBER of the arrray :");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			n=Integer.parseInt(in.readLine());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int [] toSortedA = new int[n];
		int range = 1000000000;
		Random rand = new Random();
		for(int num=0;num<n;num++)
		{
			toSortedA[num] = rand.nextInt(1000);
		}
		System.out.println("the random numbers are "+n +" !");
	//	insertS_Lh(toSortedA);
	//	Date timsT = new Date();
	//	Date timsE = new Date();
	//	long timeInter = timsE.getTime()-timsT.getTime();
	//	System.out.println("The  sorting time is :"+timeInter+"ms");
	/*	for(int num=0;num<n;num++)
		{
			if(num%10==0){System.out.println();}
			System.out.printf("%10d ,",toSortedA[num]);
		}  */
		
		
		Date timsT1 = new Date();
	//	quickS_Lh(toSortedA,0, toSortedA.length-1);
		Date timsE1 = new Date();
		long timeInter1 = timsE1.getTime()-timsT1.getTime();
		System.out.println("The  quick sorting time is :"+timeInter1+"ms");
	
		Date timsT3 = new Date();
		insertS_Lh(toSortedA);
		Date timsE3 = new Date();
		long timeInter3 = timsE3.getTime()-timsT3.getTime();
		System.out.println("The insert sorting time is :"+timeInter3+"ms");
		
		Date timsT2 = new Date();
	//	mergeS_Lh(toSortedA,0,toSortedA.length-1);
		Date timsE2 = new Date();
		long timeInter2 = timsE2.getTime()-timsT2.getTime();
		System.out.println("The  merge sorting time is :"+timeInter2+"ms");
		
	/*	for(int num=0;num<n;num++)
		{
			if(num%10==0){System.out.println();}
			System.out.printf("%10d ,",toSortedA[num]);
		}   */
	//	mergeS_Lh(toSortedA,0,toSortedA.length);
		
	}//main Finished

}
