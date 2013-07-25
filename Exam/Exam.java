import java.lang.Object;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;

public class Exam
{
	/*public T[] sortGeneric( <T>[] list )
	{
		int size = list.length;
		T[] output = new T[];
		
		output[0] = list[0];
		
		for( int i = 1; i < size; i++ )
		{
			if( list[i] > output[i-1] )
				output[i] = list[i];
			else
			{
				int j = i;
				while( list[i] > output[j-1] )
				{
					j--;
				}
				int k = i;
				while( k > j )
				{
					output[k+1] = output[k];
					k--;
				}
				output[j] = list[i];
			}
		}
		return output;
	}*/

	public class Producer extends Thread
	{
		Queue<Integer> work;
		ArrayList<Integer> results;
		public boolean done = false;
		private Random r;
		private int count = 0;
	
		public Producer(int seed)
		{
			r = new Random(seed);
			results = new ArrayList<Integer>();
		}
	
		public int getWork()
		{
			return work.remove();
		}
	
		public void run()
		{
			if( done == false )
			{
				work.add(r.nextInt());
				if(count++ == 100)
					done = true;
			}
		}
	
		public void addResult(int r)
		{
			results.add(r);
		}
	
		public int numResults()
		{
			return results.size();
		}
	}

	public class Consumer extends Thread
	{
		private Producer p;
	
		public Consumer(Producer p)
		{
			this.p = p;
		}
	
		public void run()
		{
			int toTest = p.getWork();
			boolean result = true;
			for( int i = 2; i < toTest; i++ )
			{
				if( ( toTest % i ) == 0 )
					result = false;
			}
			p.addResult(toTest);
		}
	}

	public static void printMethods( Object o, Writer output)
	{
		try{
			Method[] methods = o.getClass().getDeclaredMethods();
			output.write("class " + o.getClass().getName() + "{\n");
			for( int i = 0; i < methods.length; i++ )
			{
				output.write("\t"+ methods[i].getGenericReturnType() + " " + 
							methods[i].getName() + "(" + ");\n");
			
			}
			output.write("}");
		} catch(java.io.IOException e){}
	}
}


