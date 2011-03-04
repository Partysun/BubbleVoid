package testAndroid.partysun;

public interface ICollidable
{
	public final int GOODBUBBLE = 0;
	public final int BADBUBBLE = 1;

	/**
	 * @param type the type to set
	 */
	public void setType(int type);

	/**
	 * @return the type
	 */
	public int getType();
}
