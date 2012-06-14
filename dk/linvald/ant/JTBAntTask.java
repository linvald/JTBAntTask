package dk.linvald.ant;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.JavaEnvUtils;


/*
 * Created on 10-10-2003 by Jesper Linvald
 * This class should integrate JTB into ANT
 */
/**
 * @author jesper
 */
public class JTBAntTask extends Task {

	private File target          = null;
	private File jtbHome      = null;	
	private CommandlineJava cmd = new CommandlineJava();
	
	public JTBAntTask() {
		   cmd.setVm(JavaEnvUtils.getJreExecutable("java"));
		   try {
			   Class.forName("EDU.purdue.jtb.JTB");
			   cmd.setClassname("EDU.purdue.jtb.JTB");
		   } catch (ClassNotFoundException e) {
			   cmd.setClassname("EDU.purdue.jtb.JTB");
		   } catch (Exception e) {
			   e.printStackTrace();   
		   }   
	}
	
	// The method executing the task
	public void execute() throws BuildException {
		
		
		if (target == null || !target.isFile()) {
			throw new BuildException("Invalid target: " + target);
		}
		String arg = "-printer ";
		cmd.createArgument().setValue(arg.trim());
		cmd.createArgument().setValue(target.getAbsolutePath());
		System.out.println("Running JTB on target: " + target.getAbsolutePath());
		System.out.println("Command is:" + cmd.toString());
	
		final Path classpath = cmd.createClasspath(project);
		final File jtbJar = JTBAntTask.getArchiveFile(jtbHome);
		
		classpath.createPathElement().setPath(jtbJar.getAbsolutePath());
		classpath.addJavaRuntime();
	
		Execute.runCommand(this, cmd.getCommandline());
	}


	/**
	 * The grammar file to process.
	 */
	public void setTarget(File target) {
		this.target = target;
	}
	/**
	 * The directory containing the JTB distribution.
	 */
	public void setJtbhome(File jtbhome) {
		this.jtbHome = jtbhome;
	}
	
	protected static File getArchiveFile(File home) throws BuildException {
		if (home == null || !home.isDirectory()) {
			throw new BuildException("JTB home must be a valid directory.");
		}
		File f = new File(home, "jtb.jar");
		if (f.exists()){
		  return f;
		}
		throw new BuildException("Could not find a path to Jtb.jar from '" + home + "'.");
	}

}
