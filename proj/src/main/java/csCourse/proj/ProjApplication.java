package csCourse.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjApplication.class, args);
//		double[][] edges = {{0,1,5},{0,3,3},{1,1,7},{1,2,2},{1,3,8},{2,3,10},{2,1,6}};
//		ControlSystem CS = new ControlSystem(4,edges);
////		System.out.println("marooo");
//
//
//		CS.forwardPaths();
//		CS.loops();
//		for (Trail loop: CS.loops)
//		{
//			if(loop != null) {
//				System.out.println("the loop " + loop.getNodes().toString());
//				System.out.println("the gain " + loop.getGain());
//			}
//		}

	}

}
