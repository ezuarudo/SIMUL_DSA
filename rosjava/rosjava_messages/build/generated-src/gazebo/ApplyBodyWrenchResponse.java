package gazebo;

public interface ApplyBodyWrenchResponse extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "gazebo/ApplyBodyWrenchResponse";
  static final java.lang.String _DEFINITION = "bool success                              # return true if set wrench successful\nstring status_message                     # comments if available";
  boolean getSuccess();
  void setSuccess(boolean value);
  java.lang.String getStatusMessage();
  void setStatusMessage(java.lang.String value);
}
