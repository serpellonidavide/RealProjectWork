package test;

import java.io.IOException;

public class LinearizedLoss {
	
	
	public static double Mean(Portfolio savings, int delta_t) throws IOException {
		double sum = 0;
		for(int i=0; i < savings.numberofAssets; i++) {
			double ExpectationXasset = savings.getInitialState(savings.History)[i]
					*(-Math.pow(savings.volatilities(savings.History)[i], 2)/2)*delta_t;
			sum =+ ExpectationXasset;
			
		}
		return -sum;
		}
	
	public static double Variance(Portfolio savings, int delta_t) throws IOException {
		
		double sum = 0;
		if(savings.numberofAssets==1) {
			double d = savings.volatilities[0]*savings.volatilities[0]*savings.getInitialState(savings.History)[0]
					*savings.getInitialState(savings.History)[0]*delta_t;
			return d;
		} else {
		for(int i=0; i < savings.numberofAssets; i++) {
			for(int j=0; j< savings.numberofAssets; j++) {
				double c = savings.getInitialState(savings.History)[i]*savings.getInitialState(savings.History)[j]*
						savings.logcorrelationMatrix[i][j]*savings.volatilities[i]*savings.volatilities[j]*delta_t;
				sum =+ c;
			}
			
		}
		return sum;
		
		
	}
}
}
	
	
	


