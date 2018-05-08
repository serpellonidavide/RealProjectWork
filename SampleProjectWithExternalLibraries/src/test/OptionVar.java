package test;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.RandomVariable;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationInterface;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.stochastic.RandomVariableInterface;


public class OptionVar extends EuropeanOption{
	
	public AssetModelMonteCarloSimulationInterface model;
	public StockParams stock1;
	public double maturity;
	public double strike;

	public OptionVar(StockParams stock1, MonteCarloBlackScholesModel model, double maturity, double strike) {
		super(maturity, strike);
		this.model=model;
		this.stock1=stock1;
		this.maturity=maturity;
		this.strike=strike;
	}

	private RandomVariableInterface getDeltaLogPricesSimulation(double time) throws CalculationException {
		
		RandomVariableInterface got = model.getAssetValue(time, 0).log().sub(Math.log(stock1.InitialValue));
		
		return got;
		
	}
	
	public double getVarFullValuation(double time, AssetModelMonteCarloSimulationInterface model) throws CalculationException {
		
		double BS = AnalyticFormulas.blackScholesOptionValue(stock1.InitialValue,0,stock1.volatility,365,strike);
		
		RandomVariableInterface simulation = model.getAssetValue(time, 0);
		double[] vector = new double[simulation.size()];
		for( int i = 0; i < simulation.size(); i++) {
			
			double pricesimulation = simulation.get(i);
			vector[i] = -(AnalyticFormulas.blackScholesOptionValue(pricesimulation,0,stock1.volatility,365-time,strike)-BS);
			
		}
		
		RandomVariableInterface loss = new RandomVariable(time,vector);
		return loss.getQuantile(0.05);
		
	}
	
	public double gerVarDeltaGamma(double time, double alpha) throws CalculationException {
		
		
		RandomVariableInterface pricesimulation = getDeltaLogPricesSimulation(time);
		double delta = AnalyticFormulas.blackScholesOptionDelta(stock1.InitialValue, 0.0, stock1.volatility, this.maturity, this.strike);
		double gamma = AnalyticFormulas.blackScholesOptionGamma(stock1.InitialValue, 0.0, stock1.volatility, this.maturity, this.strike);
		RandomVariableInterface firstaproximation = pricesimulation.mult(delta).mult(stock1.InitialValue);
		RandomVariableInterface secondaproximation = pricesimulation.pow(2).mult(stock1.InitialValue*(gamma*stock1.InitialValue+delta)).div(2);
		
		RandomVariableInterface loss = (firstaproximation.add(secondaproximation)).mult(-1);
		
		return loss.getQuantile(alpha);
		
	}
	public double gerVarDelta(double time, double alpha) throws CalculationException {
		
		
		RandomVariableInterface pricesimulation = getDeltaLogPricesSimulation(time);
		double delta = AnalyticFormulas.blackScholesOptionDelta(stock1.InitialValue, 0.0, stock1.volatility, this.maturity, this.strike);
	
		RandomVariableInterface firstaproximation = pricesimulation.mult(delta).mult(stock1.InitialValue);
	
		RandomVariableInterface loss = firstaproximation.mult(-1);
		
		return loss.getQuantile(alpha);
		
		
	}
	
}
	
	
	
	

	
	


