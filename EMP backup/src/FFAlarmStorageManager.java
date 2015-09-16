//+----------------------------------------------------------------------------
//| FFAlarmStorageManager.java
//|
//| Copyright (c) 2003 - Alcatel - All rights reserved.
//+----------------------------------------------------------------------------
//| Project : EMP
//| Component : CORE
//| Package: alarm
//+--------------+------------+------------------------------------------------
//| Author       | Date       | FR/CR comment
//+--------------+------------+------------------------------------------------
//| B.THEETEN    | 18/03/03   | Creation
//+--------------+------------+------------------------------------------------
//| O. Le Berre  | 28/04/03   |  NMUes06029 - Rename tmp alarm persistent file
//+--------------+------------+------------------------------------------------
//| G. Burnside  | 10/02/04   | NMUes10340 file descriptor leak in compress()
//+--------------+------------+------------------------------------------------
//| L.BenBelgacem| 06/01/05   | NMUes12128 constructor with NE name
//+--------------+------------+------------------------------------------------
//| M. SHABOU    | 07/06/06   | TUNis33449 new add of a vector
//+--------------+------------+------------------------------------------------

/*package com.alcatel.almap.emp.alarm;

import com.alcatel.almap.emp.event.NotifEvent;
import com.alcatel.almap.emp.servant.ManagementSystem;
import com.alcatel.almap.emp.utils.DateUtils;
import com.alcatel.almap.emp.utils.StringUtils;
import com.alcatel.almap.emp.config.Config;
import com.alcatel.almap.emp.utils.log.Log;
import com.alcatel.almap.emp.corba.EmpCommon.NameAndStringValue;
import com.alcatel.almap.ITU_X721.ThresholdInfo;
import com.alcatel.almap.ITU_X721.ThresholdIndication;
 */
import java.util.StringTokenizer;
import java.util.Vector;
import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * The flat-file implementation of the alarm storage manager interface.  An 
 * instance of this class persists alarms in a flat-file.
 **/
public class FFAlarmStorageManager //implements IAlarmStorageManager
{



	public static final String MODULE = "ALM";
	private static final String FILENAME = "apt.db";
	private static final int MAX_NUM_DELETED = 10;
	//private static final String NOVALUE = "<novalue>";
	private static final String NOVALUE = "";
	private File file_ = null;
	private RandomAccessFile db_ = null;
	private long numDeleted_ = 0;

	/**
	 * Creates a new flat-file alarm storage manager using the default flat-file
	 * name: <code>$(EML.Root)/$(EML.DataDir)/apt.db</code>, where both 
	 * $(EML.Root) and $(EML.DataDir) are taken from config.properties.  If the 
	 * file already exists, alarms are appended to it.
	 **/



	/*
  public FFAlarmStorageManager()
  {
    final String METHOD = "FFAlarmStorageManager.<init>";
    String filename = Config.getProperty("EML.Root") + File.separator +
      Config.getProperty("EML.DataDir") + File.separator +
      ManagementSystem.instance().getId() + File.separator + FILENAME;
    file_ = new File(filename);
    File parent = new File(file_.getParent());
    if (!parent.exists())
      parent.mkdirs();
    try
    {
      if (!file_.exists() && !file_.createNewFile())
      {
	Log.trace(MODULE,METHOD,"Failed creating new file [" + filename + 
		  "] => can't persist alarm list");
	file_ = null;
      }
      if (!file_.canWrite())
      {
	Log.error(MODULE,METHOD,"Can't open file [" + filename +
		  "] with write access => can't persist alarm list");
	file_ = null;
      }
      else
      {
	db_ = new RandomAccessFile(file_,"rw");
      }
    }
    catch(IOException e)
    {
      Log.error(MODULE,METHOD,e.getMessage());
      db_ = null;
    }
    Log.trace(MODULE,METHOD,"Flat-file alarm storage manager created in [" +
      file_.getAbsolutePath() + "]");
  }


	 */


	/**
	 * Creates a new flat-file alarm storage manager using the NE name passed in
	 * parameter for the flat-file name:
	 * <code>$(EML.Root)/$(EML.DataDir)/neIdapt.db</code>, where both 
	 * $(EML.Root) and $(EML.DataDir) are taken from config.properties.  If the 
	 * file already exists, alarms are appended to it.
	 **/


	/*
  public FFAlarmStorageManager(String neId)
  {
    final String METHOD = "FFAlarmStorageManager.<init>";
    String filename = Config.getProperty("EML.Root") + File.separator +
      Config.getProperty("EML.DataDir") + File.separator +
      ManagementSystem.instance().getId() + File.separator + neId + FILENAME;
    file_ = new File(filename);
    File parent = new File(file_.getParent());
    if (!parent.exists())
      parent.mkdirs();
    try
    {
      if (!file_.exists() && !file_.createNewFile())
      {
	Log.trace(MODULE,METHOD,"Failed creating new file [" + filename + 
		  "] => can't persist alarm list");
	file_ = null;
      }
      if (!file_.canWrite())
      {
	Log.error(MODULE,METHOD,"Can't open file [" + filename +
		  "] with write access => can't persist alarm list");
	file_ = null;
      }
      else
      {
	db_ = new RandomAccessFile(file_,"rw");
      }
    }
    catch(IOException e)
    {
      Log.error(MODULE,METHOD,e.getMessage());
      db_ = null;
    }
    Log.trace(MODULE,METHOD,"Flat-file alarm storage manager created in [" +
      file_.getAbsolutePath() + "]");
  }


	 */

	/**
	 * Creates a new flat-file alarm storage manager using the given file.  If
	 * the file already exists, new alarms are appended to it.
	 **/

	/*
  public FFAlarmStorageManager(File file)
  {
    final String METHOD = "FFAlarmStorageManager.<init>";
    file_ = file;
    try
    {
      if (!file_.exists() && !file_.createNewFile())
      {
	Log.trace(MODULE,METHOD,"Failed creating new file [" +
		  file.getAbsolutePath() + "] => can't persist alarm list");
	file_ = null;
      }
      if (!file.canWrite())
      {
	Log.error(MODULE,METHOD,"Can't open file [" + file.getAbsolutePath() +
		  "] with write access => can't persist alarm list");
	file_ = null;
      }
      else
      {
	db_ = new RandomAccessFile(file_,"rw");
      }
    }
    catch(IOException e)
    {
      Log.error(MODULE,METHOD,e.getMessage());
      db_ = null;
    }
  }

	 */

	/**
	 * Returns true if alarms can be persisted to flat-file, else false.  Alarms
	 * cannot be persisted if the file couldn't be created with write access.
	 **/

	/*
  public boolean isValid()
  {
    return (file_!=null && db_!=null);
  }
	 */


	/**
	 * Adds a vector to persistent storage with unknown source.  A source
	 * should be given if it should be possible to automatically clear all 
	 * alarms that were reported by the given source.
	 * <p>
	 * @param vector  the vector to be persisted.
	 * @return true if the vector was added, else false.
	 **/





	public synchronized boolean add(Vector alarmList)
	{
		final String METHOD = "FFAlarmStorageManager.add";

		String moi, et, ps, pc, sp, ul, time, at, ti;
		String[] sps;
		ThresholdInfo info;

		NotifEvent alarm;

		Log.trace(MODULE,METHOD,"The size of the vector to be persisted is :  " + alarmList.size());
		try
		{
			db_.seek(db_.length());
		}	
		catch(IOException e)
		{
			Log.exception(MODULE,METHOD,e);
			return false;
		}


		for (int i = 0; i < alarmList.size(); i++)
		{
			alarm = (NotifEvent)alarmList.get(i);

			moi = StringUtils.entityIdToString(alarm.getFullNameAsEntityId());
			if (moi == null)
			{
				Log.error(MODULE,METHOD,"missing MOI => alarm ignored");
				return false;
			}
			et = alarm.getEventCategory();
			if (et == null)
			{
				Log.error(MODULE,METHOD,"missing Event Type => alarm ignored");
				return false;
			}
			ps = alarm.getSeverityAsString();
			if (ps == null)
			{
				Log.error(MODULE,METHOD,"missing Perceived Severity => alarm ignored");
				return false;
			}
			pc = alarm.getProbableCause();
			if (pc == null)
			{
				Log.error(MODULE,METHOD,"missing Probable Cause => alarm ignored");
				return false;
			}
			sp = NOVALUE;
			sps = alarm.getSpecificProblems();
			if (sps != null)
				sp = StringUtils.stringSeqToString(sps);
			//LBB TUNis26233 String ul = alarm.getFriendlyName();
			ul = alarm.getUserLabel();
			if (ul == null)
				ul = NOVALUE;
			time = alarm.getEventTime();
			if (time == null)
				time = DateUtils.now();
			at = alarm.getAdditionalText();
			if (at == null)
				at = NOVALUE;
			ti = NOVALUE;
			info = alarm.getThresholdInfo();
			if (info != null)
				ti = thresholdInfoToString(info);

			try
			{
				db_.writeBoolean(true);
				db_.writeUTF(moi + "\t" + et + "\t" + pc + "\t" + sp + "\t" + ps + 
						"\t" + ul + "\t" + time + "\t" + at + "\t" + ti + 
						"\t" + getSourceKey("unknown") + "\n");
			}
			catch(IOException e)
			{
				Log.exception(MODULE,METHOD,e);
				return false;
			}
		}
		return true;
	}
	/**
	 * Adds a new alarm to persistent storage.
	 * <p>
	 * @param moi  the managed object instance (stringified)
	 * @param et   the event type
	 * @param ps   the perceived severity
	 * @param pc   the probable cause
	 * @param sp   the specific problems (stringified)
	 * @param ul   the user label (friendly name)
	 * @param time the alarm generation time
	 * @param at   the additional text
	 * @param ti   the threshold information (stringified)
	 * @param src  the source of the alarm (who generated it) or null if this
	 *             knowledge is unimportant.
	 * @return true if the alarm was added, else false.  E.g. false is returned
	 *         when the alarm is a duplicate.
	 **/
	
	/*
	public synchronized boolean add(String moi,String et,String ps,String pc,
			String sp,String ul,String time,String at,
			String ti,String src)
	{
		final String METHOD = "FFAlarmStorageManager.add";
		// first check if the alarm is not yet there
		if (get(moi,et,ps,pc,sp) != null)
			Log.trace(MODULE,METHOD,"Alarm already exists => ignoring");
		else
		{
			try
			{
				db_.seek(db_.length());
				db_.writeBoolean(true);
				db_.writeUTF(moi + "\t" + et + "\t" + pc + "\t" + sp + "\t" + ps + 
						"\t" + ul + "\t" + time + "\t" + at + "\t" + ti + 
						"\t" + getSourceKey(src) + "\n");
				return true;
			}
			catch(IOException e)
			{
				Log.exception(MODULE,METHOD,e);
			}
		}
		return false;
	}
	
	*/

	/**
	 * Adds a new alarm to persistent storage with unknown source.  A source
	 * should be given if it should be possible to automatically clear all 
	 * alarms that were reported by the given source.
	 * <p>
	 * @param alarm  the alarm to be persisted.
	 * @return true if the alarm was added, else false.  E.g. false is returned
	 *         when the alarm is a duplicate.
	 **/
	
	/*
	public boolean add(NotifEvent alarm)
	{
		return add(alarm,"unknown");
	}
	*/
	/**
	 * Adds a new alarm to persistent storage with given source.  The source 
	 * can be given to allow for automatically clearing all alarms belonging to
	 * the given source.
	 * <p>
	 * @param alarm  the alarm to be persisted.
	 * @param src  the source of the alarm (who generated it) or null if this
	 *             knowledge is unimportant.
	 * @return true if the alarm was added, else false.  E.g. false is returned
	 *         when the alarm is a duplicate.
	 **/
	
	/*
	public boolean add(NotifEvent alarm,String src)
	{
		final String METHOD = "FFAlarmStorageManager.add";
		String moi = StringUtils.entityIdToString(alarm.getFullNameAsEntityId());
		if (moi == null)
		{
			Log.error(MODULE,METHOD,"missing MOI => alarm ignored");
			return false;
		}
		String et = alarm.getEventCategory();
		if (et == null)
		{
			Log.error(MODULE,METHOD,"missing Event Type => alarm ignored");
			return false;
		}
		String ps = alarm.getSeverityAsString();
		if (ps == null)
		{
			Log.error(MODULE,METHOD,"missing Perceived Severity => alarm ignored");
			return false;
		}
		String pc = alarm.getProbableCause();
		if (pc == null)
		{
			Log.error(MODULE,METHOD,"missing Probable Cause => alarm ignored");
			return false;
		}
		String sp = NOVALUE;
		String[] sps = alarm.getSpecificProblems();
		if (sps != null)
			sp = StringUtils.stringSeqToString(sps);
		//LBB TUNis26233 String ul = alarm.getFriendlyName();
		String ul = alarm.getUserLabel();
		if (ul == null)
			ul = NOVALUE;
		String time = alarm.getEventTime();
		if (time == null)
			time = DateUtils.now();
		String at = alarm.getAdditionalText();
		if (at == null)
			at = NOVALUE;
		String ti = NOVALUE;
		ThresholdInfo info = alarm.getThresholdInfo();
		if (info != null)
			ti = thresholdInfoToString(info);
		return add(moi,et,ps,pc,sp,ul,time,at,ti,src);
	}

	*/
	
	/**
	 * Deletes the record with given attribute values from persistent storage.
	 * <p>
	 * @param moi  the managed object instance (stringified)
	 * @param et   the event type
	 * @param ps   the perceived severity
	 * @param pc   the probable cause
	 * @param sp   the specific problems (stringified)
	 * @return true if such a record existed, else false.
	 **/
	/*
	public synchronized boolean delete(String moi,String et,String ps,String pc,String sp)
	{
		final String METHOD = "FFAlarmStorageManager.delete";
		String partialAlarmStr = moi + "\t" + et + "\t" + pc + "\t" + sp;
		try
		{
			db_.seek(0);
			long len = db_.length();
			long recStart = 0;
			while ((recStart = db_.getFilePointer()) < len)
			{
				try
				{
					boolean skip = !db_.readBoolean();
					String line = db_.readUTF();
					if (!skip)
					{
						if (line.startsWith(partialAlarmStr))
						{
							db_.seek(recStart);
							db_.writeBoolean(false);
							Log.trace(MODULE,METHOD,"alarm [" + line + "] deleted");
							numDeleted_++;
							if (numDeleted_++ >= MAX_NUM_DELETED)
								compress();
							return true;
						}
					}
				}
				catch(IOException e)
				{
					Log.exception(MODULE,METHOD,e);
				}
			}
		}
		catch(IOException e)
		{
			Log.exception(MODULE,METHOD,e);
		}
		Log.trace(MODULE,METHOD,"No such alarm found [" + partialAlarmStr + "]");
		return false;
	}
	*/

	/**
	 * Deletes the record corresponding to the given alarm from persistent
	 * storage.
	 * <p>
	 * @param alarm  the alarm to be deleted
	 * @return true if such a record existed, else false.
	 **/
	/*
	public boolean delete(NotifEvent alarm)
	{
		final String METHOD = "FFAlarmStorageManager.delete";
		String sp = NOVALUE;
		String[] sps = alarm.getSpecificProblems();
		if (sps != null)
			sp = StringUtils.stringSeqToString(sps);
		return delete(StringUtils.entityIdToString(alarm.getFullNameAsEntityId()),
				alarm.getEventCategory(),
				alarm.getSeverityAsString(),
				alarm.getProbableCause(),
				sp);
	}
	*/

	/**
	 * Deletes the records corresponding to the alarms generated by the given
	 * source.  If no source was given to the add() methods, this functionality
	 * is not available.
	 * <p>
	 * @param src  the source that generated the alarm
	 **/
	
	/*
	public void deleteAlarmsBySource(String src)
	{
		final String METHOD = "FFAlarmStorageManager.delete";
		String source = getSourceKey(src);
		try
		{
			db_.seek(0);
			long len = db_.length();
			long recStart = 0;
			long recPos = 0;
			while ((recStart = db_.getFilePointer()) < len)
			{
				try
				{
					boolean skip = !db_.readBoolean();
					String line = db_.readUTF();
					recPos = db_.getFilePointer();
					if (!skip)
					{
						if (line.indexOf(source) != -1)
						{
							db_.seek(recStart);
							db_.writeBoolean(false);
							Log.trace(MODULE,METHOD,"alarm [" + line + "] deleted");
							numDeleted_++;
							db_.seek(recPos);
						}
					}
				}
				catch(IOException e)
				{
					Log.exception(MODULE,METHOD,e);
				}
			}
			if (numDeleted_ >= MAX_NUM_DELETED)
				compress();
		}
		catch(IOException e)
		{
			Log.exception(MODULE,METHOD,e);
		}
		Log.trace(MODULE,METHOD,"No such alarms found");
	}
	*/

	/**
	 * Gets the alarm matching the given argument values.
	 * <p> 
	 * @param moi  the managed object instance (stringified)
	 * @param et   the event type
	 * @param ps   the perceived severity
	 * @param pc   the probable cause
	 * @param sp   the specific problems (stringified)
	 * @return  the alarm generated on the given object or null if no such alarm
	 *          was found.  If multiple alarms are matching, the first is
	 *          returned.  Note that finding multiple matching alarms is an
	 *          erronous situation.
	 **/
	
	/*
	public NotifEvent get(String moi,String et,String ps,String pc,String sp)
	{
		final String METHOD = "FFAlarmStorageManager.get";
		String partialAlarmStr = moi + "\t" + et + "\t" + pc + "\t" + sp + "\t" +
				ps;
		NotifEvent[] alarms = getByKey(partialAlarmStr);
		if (alarms.length > 1)
			Log.error(MODULE,METHOD,"Multiple matching alarms");
		if (alarms.length == 0)
			return null;
		return alarms[0];
	}
	*/

	/**
	 * Gets all alarms generated on the given object or on a descendant.
	 * <p>
	 * @param moi  the managed object instance (stringified)
	 * @return  the array of alarms generated on the given object.
	 **/
	
	/*
	public NotifEvent[] get(String moi)
	{
		return getByKey(moi);
	}

	private synchronized NotifEvent[] getByKey(String key)
	{
		final String METHOD = "FFAlarmStorageManager.getByKey";
		Vector v = new Vector();
		try
		{
			db_.seek(0);
			long len = db_.length();
			while (db_.getFilePointer() < len)
			{
				try
				{
					boolean skip = !db_.readBoolean();
					String line = db_.readUTF();
					if (!skip)
					{
						//System.out.println("Checking [" + line + "] against [" + key + "]");
						if (line.startsWith(key))
						{
							Log.trace(MODULE,METHOD,"alarm [" + line + "] matches");
							NotifEvent alarm = convert(line);
							v.add(alarm);
						}
					}
				}
				catch(IOException e)
				{
					Log.error(MODULE,METHOD,e.getMessage());
				}
			}
		}
		catch(IOException e)
		{
			Log.exception(MODULE,METHOD,e);
		}
		NotifEvent[] alarms = new NotifEvent[v.size()];
		for (int i=0;i<v.size();i++)
			alarms[i] = (NotifEvent)v.elementAt(i);
		return alarms;
	}
	*/

	/**
	 * Gets all alarms generated by the given source.
	 * <p>
	 * @param src  the source that generated the alarm
	 * @return  the array of alarms generated by the given source.
	 **/
	
	/*
	public synchronized NotifEvent[] getAlarmsBySource(String src)
	{
		final String METHOD = "FFAlarmStorageManager.get";
		Vector v = new Vector();
		String source = getSourceKey(src);
		try
		{
			db_.seek(0);
			long len = db_.length();
			while (db_.getFilePointer() < len)
			{
				try
				{
					boolean skip = !db_.readBoolean();
					String line = db_.readUTF();
					if (!skip)
					{
						if (line.indexOf(source) != -1)
						{
							Log.trace(MODULE,METHOD,"alarm [" + line + "] matches");
							NotifEvent alarm = convert(line);
							v.add(alarm);
						}
					}
				}
				catch(IOException e)
				{
					Log.error(MODULE,METHOD,e.getMessage());
				}
			}
		}
		catch(IOException e)
		{
			Log.exception(MODULE,METHOD,e);
		}
		NotifEvent[] alarms = new NotifEvent[v.size()];
		for (int i=0;i<v.size();i++)
			alarms[i] = (NotifEvent)v.elementAt(i);
		return alarms;
	}
	*/

	/**
	 * Gets all alarms in persistent storage.
	 * <p>
	 * @return the array of alarms in persistent storage.
	 **/
	
	/*
	public synchronized NotifEvent[] getAll()
	{
		final String METHOD = "FFAlarmStorageManager.getAll";
		Vector v = new Vector();
		try
		{
			db_.seek(0);
			long len = db_.length();
			while (db_.getFilePointer() < len)
			{
				try
				{
					boolean skip = !db_.readBoolean();
					String line = db_.readUTF();
					if (!skip)
					{
						Log.trace(MODULE,METHOD,"converting alarm [" + line + "]");
						NotifEvent alarm = convert(line);
						v.add(alarm);
					}
				}
				catch(IOException e)
				{
					Log.error(MODULE,METHOD,e.getMessage());
				}
			}
		}
		catch(IOException e)
		{
			Log.exception(MODULE,METHOD,e);
		}
		Log.trace(MODULE,METHOD,"All alarms read");
		NotifEvent[] alarms = new NotifEvent[v.size()];
		for (int i=0;i<v.size();i++)
			alarms[i] = (NotifEvent)v.elementAt(i);
		return alarms;
	}
	*/

	/**
	 * Closes the database.
	 **/
	/*
	protected void finalize()
			throws Throwable
	{
		db_.close();
	}
	*/

	/**
	 * Converts a record string into a NotifEvent.  The record string has the
	 * format: 
	 *   <moi>\t<et>\t<ps>\t<pc>\t<sp>\t<ul>\t<time>\t<at>\t<ti>
	 **/
	
	private NotifEvent convert(String line)
	{
		final String METHOD = "FFAlarmStorageManager.convert";
		Vector v = StringUtils.tokenize(line,"\t");
		if (v.size() != 10)
		{
			Log.error(MODULE,METHOD,"Wrong line format: expected 10 tokens, got " +
					v.size());
			return null;
		}
		NameAndStringValue[] moi = 
				StringUtils.stringToEntityId((String)v.elementAt(0));
		String et = (String)v.elementAt(1);
		String pc = (String)v.elementAt(2);
		String sporg = (String)v.elementAt(3);
		String[] sp = StringUtils.tokenizeWithDelimiter((String)v.elementAt(3),",");
		String ps = (String)v.elementAt(4);
		String ul = (String)v.elementAt(5);
		String time = (String)v.elementAt(6);
		String at = (String)v.elementAt(7);
		ThresholdInfo ti = toThresholdInfo((String)v.elementAt(8));
		NotifEvent alarm = NotifEvent.almapAlarm(moi,et,pc,ps,time);
		alarm.setGenerated();
		if (!sporg.equals(NOVALUE)) 
			alarm.setSpecificProblems(sp);
		//alarm.setFriendlyName(ul);
		alarm.setUserLabel(ul);
		alarm.setAdditionalText(at);
		if (ti != null)
			alarm.setThresholdInfo(ti);
		return alarm;
	}

	/**
	 * Converts a threshold info structure into a string of the format:
	 *   <String:id>,<float:observed>,<int:indication>,<float:high>,<float:low>,
	 *   <String:time>
	 **/
	
	/*
	private String thresholdInfoToString(ThresholdInfo ti)
	{
		return new String(ti.attribute_identifier + "," + ti.observed_value + "," +
				ti.indication.value() + "," + ti.high + "," + ti.low + "," +
				ti.arm_time);
	}
	*/

	/**
	 * Converts the string in the format:
	 *   <String:id>,<float:observed>,<int:indication>,<float:high>,<float:low>,
	 *   <String:time>
	 * into a ThresholdInfo object.  Returns null if no conversion could be
	 * performed, e.g. when the given input parameter corresponds to NOVALUE.
	 **/
	
	/*
	private ThresholdInfo toThresholdInfo(String ti)
	{
		final String METHOD = "FFAlarmStorageManager.toThresholdInfo";
		StringTokenizer st = new StringTokenizer(ti,",");
		if (ti.equals(NOVALUE))
			return null;
		if (st.countTokens() != 6)
		{
			Log.error(MODULE,METHOD,"Wrong number of tokens in [" + ti + 
					"]: expected 6, got " + 
					st.countTokens());
			return null;
		}
		try
		{
			return new ThresholdInfo(st.nextToken(),
					Float.parseFloat(st.nextToken()),
					ThresholdIndication.from_int(Integer.parseInt(st.nextToken())),
					Float.parseFloat(st.nextToken()),
					Float.parseFloat(st.nextToken()),
					st.nextToken());
		}
		catch(NumberFormatException e)
		{
			Log.error(MODULE,METHOD,"Couldn't convert threshold info [" + ti + "]");
			return null;
		}
	}
	*/

	/**
	 * Converts a sequence of ManagementExtensionTypes into a string of the
	 * format:
	 *    
  private String additionalInfoToString(ManagementExtensionType[] addInfo)
  {
  }

  private ManagementExtensionType[] toAdditionalInfo(String addInfo)
  {

  }
	 */

	/**
	 * Rewrites the flat-file eliminating the deleted records.
	 **/
	
	/*
	private synchronized void compress()
	{
		final String METHOD = "FFAlarmStorageManager.compress";
		RandomAccessFile tmpFile = null;
		try
		{
			File tmp = new File(file_.getAbsolutePath() +".tmp");
			if (!tmp.createNewFile())
				Log.error(MODULE,METHOD,"Couldn't compress, the file [" +
						tmp.getAbsolutePath() + "] already exists");
			tmpFile = new RandomAccessFile(tmp,"rw");
			db_.seek(0);
			long len = db_.length();
			while (db_.getFilePointer() < len)
			{
				try
				{
					boolean skip = !db_.readBoolean();
					String line = db_.readUTF();
					if (!skip)
					{
						tmpFile.writeBoolean(true);
						tmpFile.writeUTF(line);
					}
				}
				catch(IOException e)
				{
					Log.error(MODULE,METHOD,e.getMessage());
				}
			}
			Log.trace(MODULE,METHOD,"temporary file written");
			tmpFile.close();
			String filename = file_.getAbsolutePath();
			File oldFile = new File(filename + ".old");
			file_.renameTo(oldFile);
			Log.trace(MODULE,METHOD,"current file renamed to [" + 
					oldFile.getAbsolutePath() + "]");
			File newFile = new File(filename);
			tmp.renameTo(newFile);
			// NMUes06029 - Rename tmp alarm persistent file
			file_ = newFile;
			// NMUes10340: file descriptor leak, need to call close()
			// on old instance prior to assigning a new instance:
			db_.close();
			db_ = new RandomAccessFile(file_,"rw");
			Log.trace(MODULE,METHOD,"finished");
			numDeleted_ = 0;
		}
		catch(IOException e)
		{
			Log.exception(MODULE,METHOD,e);
			if (tmpFile != null)
				try { tmpFile.close(); } catch (IOException ioe) { /* whatever */ /*};
		}
	}
	*/

	/**
	 * Constructs a key for the given source.
	 **/
	
	/*
	private String getSourceKey(String src)
	{
		if (src == null)
			src = "unknown";
		return new String("<source:" + src + ">");
	}
	*/
}
