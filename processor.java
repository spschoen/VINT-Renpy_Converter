import java.util.*;
import java.io.*;

public class processor {

	public static void main(String[] args) throws IOException {
	
		if ( args.length != 2 ) {
			System.out.println("Expected Execution: processor [originalFile changedFile]");
			System.out.println("Got: processor " + Arrays.toString(args));
			System.exit(1);
		}
		
		File f = new File(args[0]);
		File g = new File(args[1]);
		if ( g.exists() ) {
			g.delete();
		}
		Scanner in = null;
		FileWriter out = null;
		try {
			in = new Scanner(f);
			out = new FileWriter(g);
		} catch (Exception e) {
			System.out.println("Shit's fucked");
			System.exit(1);
		}
		
		while (in.hasNextLine()) {
			String s = in.nextLine() + "\n";
			//System.out.print(s);
			if ( s.equals("") || s.equals("\n") || (s.charAt(0) == 32 && s.length() < 3) ) {
				// If it's blank, a new line, or starts with a space and isn't " nam" for example.
				// Then just print it out.
				out.write(s);
			} else {
				if ( s.charAt(0) == '"' ) {
					s = "mst " + s;
				} else if ( s.charAt(0) == 'b' ) {
					//b([name],[show/hide]) ==> scene/hide [name] 
					String BG = "BG_" + s.substring(s.indexOf("(") + 1,s.indexOf(","));
					//System.out.println("BCK: " + BG);
					if ( s.contains("show") ) {
						//b(labbuilding_day_spring,show)
						s = "scene " + BG + "\n";
					} else if ( s.contains("hide") ) {
						//b(campus3_day_spring,hide)
						s = "hide " + BG + "\n";
					}
				} else if ( s.charAt(0) == 'c' ) {
					//c([3-letter character name],[sprite name],[show/hide]) ==> show/hide [sprite name]
					String[] broken = s.split(",");
					String sprite = broken[1];
					if ( sprite.contains(")") ) {
						sprite = sprite.substring(0,sprite.indexOf(")"));
					}
					//System.out.println("SPR: " + sprite);
					if ( !s.contains("hide")  ) {
						//b(labbuilding_day_spring,show)
						s = "show " + sprite + "\n";
					} else {
						//b(campus3_day_spring,hide)
						s = "hide " + sprite + "\n";
					}
				} else if ( s.substring(0,3).equals("#sc") ) {
					//sc([name],[flag name],[+1/-2etc]) ==> label [name]:
					String label = "";
					if( s.contains(",") ) {
						label = s.substring(s.indexOf("(") + 1, s.indexOf(","));
					} else {
						label = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
					}
					//System.out.println("LBL: " + label);
					s = label + ":";
				}
				if ( s.charAt(s.length() - 1) == ':' ) {
					out.write("\n\n" + s + "\n");
				} else {
					out.write("\t" + s);
				}
			}
		}
		in.close();
	
	}

}