package api;

public class GeoLoc implements geo_location{
	private double x,y,z;

	@Override
	public double x() {
		return x;
	}

	@Override
	public double y() {
		return y;
	}

	@Override
	public double z() {
		return z;
	}

	@Override
	public double distance(geo_location g) {
		 double dx = this.x() - g.x();
	     double dy = this.y() - g.y();
	     double dz = this.z() - g.z();
	     double ans = (dx*dx+dy*dy+dz*dz);
	      return Math.sqrt(ans);
	}
	
	///////constructors//////////
	public GeoLoc(double x,double y,double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
    public GeoLoc(geo_location g) {
            this.x = g.x();
            this.y = g.y();
            this.z = g.z();
        }
	  
    public GeoLoc() {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }
	
}