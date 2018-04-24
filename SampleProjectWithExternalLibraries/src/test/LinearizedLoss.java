package test;

import java.io.IOException;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.OutOfRangeException;



public class LinearizedLoss {
	
	
	public static double Mean(Portfolio savings, double delta_t) throws IOException {
		double sum = 0;
		for(int i=0; i < savings.numberofAssets; i++) {
			double ExpectationXasset = savings.initialState[i]
					*(-Math.pow(savings.volatilities[i], 2)/2)*delta_t;
			sum =+ ExpectationXasset;
			
		}
		return -sum;
		}
	
	public static double Variance(Portfolio savings, double delta_t) throws IOException {
		
		double sum = 0;
		if(savings.numberofAssets==1) {
			double d = savings.volatilities[0]*savings.volatilities[0]*savings.initialState[0]
					*savings.initialState[0]*delta_t;
			return d;
		} else {
		for(int i=0; i < savings.numberofAssets; i++) {
			for(int j=0; j< savings.numberofAssets; j++) {
				double c = savings.initialState[i]*savings.initialState[j]*
						savings.logreturncorrelationMatrix[i][j]*savings.volatilities[i]*savings.volatilities[j]*delta_t;
				sum =+ c;
			}
		}
		return sum;
		}
		}
	
	public static double getVar(Portfolio savings, double delta_t, double p) throws OutOfRangeException, IOException {
		
		NormalDistribution normal = new NormalDistribution();
		double var = LinearizedLoss.Mean(savings, delta_t)+Math.sqrt(LinearizedLoss.Variance(savings, delta_t))*
				normal.inverseCumulativeProbability(p);
		return var;
		
	}
	
}
	
	
	


