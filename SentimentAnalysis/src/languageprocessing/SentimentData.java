package languageprocessing;

public class SentimentData {
	//private String mean;
	private String opinion;
	private String sentiment;
	public SentimentData (String mean, String opinion, String sentiment) {
		//this.mean=mean;
		this.opinion=opinion;
		this.sentiment=sentiment;
	}
	public double getOpinion (){
		if(opinion.length()<2)return 0.1;
		if(opinion.contains("hypothetic"))return 0.3;
		if(opinion.contains("opinion"))return 0.6;
		if(opinion.contains("fact"))return 1.;
		return 0.;
	}
	public int getDirection() {
		if (sentiment.equalsIgnoreCase("negative"))return -1;
		if (sentiment.equalsIgnoreCase("positive"))return 1;
		return 0;
	}
	public double detSummarizedInfo() {
		double opin=getOpinion();
		double dir=getDirection();
		double sum=opin*dir;
		//if(sum==0.&&dir!=0.)return dir*0.5;
		return sum;
	}
}
