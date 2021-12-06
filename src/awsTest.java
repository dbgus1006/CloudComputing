import java.util.Collection;
import java.util.Scanner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.RebootInstancesResult;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.AvailabilityZone;




public class awsTest {
	
	static AmazonEC2 ec2;
	
	private static void init() throws Exception {
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " + 
					"Please make sure that your credentials file is at the correct " +
					"location (~/.aws/credential), and is in valid format.", e);
		}
		ec2 = AmazonEC2ClientBuilder.standard().withCredentials(credentialsProvider).withRegion("us-east-2").build();
	}
	
	public static void main(String[] args) throws Exception {
		init();
		
		Scanner menu = new Scanner(System.in);
		int number = 0;
		
		while(true)
		{
			System.out.println("                                                     ");
			System.out.println("                                                     ");
			System.out.println("-----------------------------------------------------");
			System.out.println("         Amazon AWS Control Panel using SDK          ");
			System.out.println("                                                     ");
			System.out.println("  Cloud Computing, Computer Science Department       ");
			System.out.println("                      at Chungbuk National University");
			System.out.println("-----------------------------------------------------");
			System.out.println("  1. list instance              2. available zones   ");
			System.out.println("  3. start instance             4. available regions ");
			System.out.println("  5. stop instance              6. create instance   ");
			System.out.println("  7. reboot instance            8. list images       ");
			System.out.println("                                99. quit             ");
			System.out.println("-----------------------------------------------------");
			System.out.print("Enter an integer: ");
			number = menu.nextInt();
			
			switch(number) {
			case 1:
				listInstance();
				break;
			case 2:
				availableZones();				
				break;
			case 3:
				startInstance();				
				break;
			case 4:
				availableRegions();
				break;
			case 5:
				stopInstance();
				break;
			case 6:
				createInstance();				
				break;
			case 7:
				rebootInstance();				
				break;
			case 8:
				listImage();				
				break;
			case 99:
				System.exit(0);
			}
		}
	}
	
	
	// Menu1
	public static void listInstance() {
		System.out.println();
		System.out.println("=====================================================");
		System.out.println("Listing instances....");
		boolean done = false;
		
		DescribeInstancesRequest request = new DescribeInstancesRequest();
		
		while(!done) {
			DescribeInstancesResult response = ec2.describeInstances(request);
			
			for(Reservation reservation : response.getReservations()) {
				for(Instance instance : reservation.getInstances()) {
					System.out.printf(
							"[id] %s, " +
							"[AMI] %s, " +
							"[type] %s, " +
							"[state] %10s, " +
							"[monitoring state] %s",
							instance.getInstanceId(),
							instance.getImageId(),
							instance.getInstanceType(),
							instance.getState().getName(),
							instance.getMonitoring().getState());
				}
				System.out.println();
			}
			
			request.setNextToken(response.getNextToken());
			
			if(response.getNextToken() == null) {
				done = true;
			}	
		}
		System.out.println("=====================================================");
	}
	
	// Menu2
	public static void availableZones() {
		System.out.println();
		System.out.println("=====================================================");
		System.out.println("Show Available Zones....");
				
		DescribeAvailabilityZonesResult zoneResult = ec2.describeAvailabilityZones();
		
		for(AvailabilityZone zone : zoneResult.getAvailabilityZones()) {
			System.out.printf("[Zone Name: %s] " + "[Zone State: %s] " + "[Zone Id: %s] ", zone.getZoneName(), zone.getState(), zone.getZoneId());
			System.out.println();
		}

		System.out.println("=====================================================");
	}
	
	// Menu3
	public static void startInstance() {
		System.out.println();
		System.out.println("=====================================================");
		System.out.println("Start Instances....");
		
		Scanner id_string = new Scanner(System.in);
		System.out.print("ID of the instance to start: ");
		String instanceId = id_string.nextLine();

		StartInstancesRequest startRequest = new StartInstancesRequest().withInstanceIds(instanceId);
		ec2.startInstances(startRequest);
		
		System.out.printf("[%s] Instances started", instanceId);
		System.out.println();
		System.out.println("=====================================================");
	}
	
	// Menu4
	public static void availableRegions() {
		System.out.println();
		System.out.println("=====================================================");
		System.out.println("Show Available Regions....");
				
		DescribeRegionsResult regionResult = ec2.describeRegions();
		
		for(Region region : regionResult.getRegions()) {
			System.out.printf("[Region Name: %s] " + "[Endpoint: %s] ", region.getRegionName(), region.getEndpoint());
			System.out.println();
		}
		
		System.out.println("=====================================================");
	}
	
	// Menu5
	public static void stopInstance() {
		System.out.println();
		System.out.println("=====================================================");
		System.out.println("Stop Instances....");
		
		Scanner id_string = new Scanner(System.in);
		System.out.print("ID of the instance to stop: ");
		String instanceId = id_string.nextLine();

		StopInstancesRequest stopRequest = new StopInstancesRequest().withInstanceIds(instanceId);
		ec2.stopInstances(stopRequest);
		
		System.out.printf("[%s] Instances stopped", instanceId);
		System.out.println();
		System.out.println("=====================================================");
	}
	
	// Menu6
	public static void createInstance() {
		System.out.println();
		System.out.println("=====================================================");
		System.out.println("Create Instances....");
		
		Scanner id_string = new Scanner(System.in);
		System.out.print("ID of the image: ");
		String imageId = id_string.nextLine();

		RunInstancesRequest runRequest = new RunInstancesRequest()
	            .withImageId(imageId)
	            .withInstanceType(InstanceType.T2Micro)
	            .withMaxCount(1)
	            .withMinCount(1);
		
		RunInstancesResult runResult = ec2.runInstances(runRequest);

        String reservation_id = runResult.getReservation().getInstances().get(0).getInstanceId();
		
        System.out.printf("Successfully created EC2 instance %s based on AMI %s",reservation_id, imageId);
		System.out.println();
		System.out.println("=====================================================");
	}
	
	// Menu7
	public static void rebootInstance() {
		System.out.println();
		System.out.println("=====================================================");
		System.out.println("Reboot Instances....");
		
		Scanner id_string = new Scanner(System.in);
		System.out.print("ID of the instance to reboot: ");
		String instanceId = id_string.nextLine();

		RebootInstancesRequest rebootRequest = new RebootInstancesRequest().withInstanceIds(instanceId);
		
		RebootInstancesResult rebootResult = ec2.rebootInstances(rebootRequest);
		
        System.out.printf("[%s] Instances rebooted", instanceId);
		System.out.println();
		System.out.println("=====================================================");
	}
	
	// Menu8
	public static void listImage() {
		System.out.println();
		System.out.println("=====================================================");
		System.out.println("Listing Images....");

		String ownerId = "113722351692";
		DescribeImagesRequest imageRequest = new DescribeImagesRequest().withOwners(ownerId);
		Collection<Image> images = ec2.describeImages(imageRequest).getImages();
			
		for(Image img : images) {
			System.out.printf("[ID] %s, " + "[Name] %s, " + "[State] %s, " + "[Owner] %s", img.getImageId(), img.getName(), img.getState(), img.getOwnerId());
			System.out.println();
		}	
		
		System.out.println();
		System.out.println("=====================================================");
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
