package edu.berkeley.compbio.jlibsvm.binary;

import edu.berkeley.compbio.jlibsvm.SolutionModel;
import edu.berkeley.compbio.jlibsvm.SvmParameter;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public abstract class AlphaModel<L extends Comparable, P> extends SolutionModel<P>
	{
	public Map<P, Double> supportVectors;
	//public float[] alpha;
	public float rho;

	/*	public AlphaModel(BinaryModel<L,P> alphaModel)
		 {
		 super(alphaModel);
		 supportVectors = alphaModel.supportVectors; // ** should be deep copy?
	 //	alpha = alphaModel.alpha;
		 rho = alphaModel.rho;
		 }
 */
	protected AlphaModel(KernelFunction kernel, SvmParameter param)
		{
		super(kernel, param);
		}

	protected AlphaModel()
		{
		super(); //kernel, param);
		}

	public AlphaModel(Properties props)
		{
		super(props);
		rho = Float.parseFloat(props.getProperty("rho"));
		}

	/**
	 * Remove vectors whose alpha is zero, leaving only support vectors
	 */
	public void compact()
		{
		//assert supportVectors.size() == alpha.length;

		//List<Integer> nonZeroAlphaIndexes = new ArrayList<Integer>();

		for (Iterator<Map.Entry<P, Double>> i = supportVectors.entrySet().iterator(); i.hasNext();)
			//for(Map.Entry<P,Float> entry : supportVectors.entrySet())
			{
			Map.Entry<P, Double> entry = i.next();
			if (entry.getValue() == 0)
				{
				i.remove();
				//supportVectors.remove(entry); // ** lookout for concurrent modification
				}
			}
		/*
		for (int i = 0; i < supportVectors.size(); i++)
			{
			if (Math.abs(alpha[i]) > 0)
				{
				nonZeroAlphaIndexes.add(i);
				}
			}

		List<P> newSupportVectors = new ArrayList<P>(nonZeroAlphaIndexes.size());
		float[] newAlphas = new float[nonZeroAlphaIndexes.size()];


		//int j = 0;
		for (Integer i : nonZeroAlphaIndexes)
			{
			newSupportVectors.add(supportVectors.get(i));
			newAlphas[j] = alpha[i];
			j++;
			}

		supportVectors = newSupportVectors;
		alpha = newAlphas;*/
		}

	public void writeToStream(DataOutputStream fp) throws IOException
		{
		super.writeToStream(fp);

		fp.writeBytes("rho " + rho + "\n");
		fp.writeBytes("total_sv " + supportVectors.size() + "\n");
		}


	protected void writeSupportVectors(DataOutputStream fp) throws IOException
		{
		fp.writeBytes("SV\n");

		for (Map.Entry<P, Double> entry : supportVectors.entrySet())
			{

			fp.writeBytes(entry.getValue() + " ");

			//	P p = supportVectors.get(i);

			fp.writeBytes(entry.getKey().toString());

/*			if (kernel instanceof PrecomputedKernel)
				{
				fp.writeBytes("0:" + (int) (p.values[0]));
				}
			else
				{
				for (int j = 0; j < p.indexes.length; j++)
					{
					fp.writeBytes(p.indexes[j] + ":" + p.values[j] + " ");
					}
				}*/
			fp.writeBytes("\n");
			}
		}

	protected void readSupportVectors(BufferedReader reader) throws IOException
		{
		throw new NotImplementedException();
		/*
		String line;
		int lineNo = 0;
		while ((line = reader.readLine()) != null)
			{
			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

			alpha[lineNo] = Float.parseFloat(st.nextToken());

			// ** SparseVector not generified here, bah

			int n = st.countTokens() / 2;
			SparseVector p = new SparseVector(n);
			supportVectors[lineNo] = p;
			for (int j = 0; j < n; j++)
				{
				p.indexes[j] = Integer.parseInt(st.nextToken());
				p.values[j] = Float.parseFloat(st.nextToken());
				}
			}
			*/
		}
	}