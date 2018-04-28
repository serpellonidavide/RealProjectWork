package test;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.RandomVariable;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationInterface;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.stochastic.RandomVariableInterface;

public class OptionVar extends EuropeanOption{



	public OptionVar(double maturity, double strike) {
		super(maturity, strike);
		// TODO Auto-generated constructor stub
	}

	private RandomVariableInterface getLogPricesSimulation(double time, AssetModelMonteCarloSimulationInterface model) throws CalculationException {
		
		RandomVariableInterface got = model.getAssetValue(time, 0).log();
		
		return got;
		
	}
	
	public RandomVariableInterface getOptionPayoff(double time, AssetModelMonteCarloSimulationInterface model, double strike) throws CalculationException	{
		
	RandomVariableInterface LogPricesSimulation = getLogPricesSimulation(time,model);
	double[] gg = new double[model.getNumberOfPaths()];
	
	for(int i=0; i < model.getNumberOfPaths(); i++) {
		if(LogPricesSimulation.get(i) - strike != 0) {
		gg[i] = LogPricesSimulation.get(i) - strike;
		} else {
			gg[i] =0;
				}
		}
	RandomVariable OptionPayoff = new RandomVariable(time, gg);
	
	return OptionPayoff;
	}
}


