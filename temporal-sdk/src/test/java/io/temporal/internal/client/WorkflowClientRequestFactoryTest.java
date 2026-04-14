package io.temporal.internal.client;

import static org.junit.Assert.*;

import io.temporal.api.workflowservice.v1.SignalWithStartWorkflowExecutionRequest;
import io.temporal.api.workflowservice.v1.StartWorkflowExecutionRequest;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.interceptors.Header;
import java.util.Collections;
import org.junit.Test;

public class WorkflowClientRequestFactoryTest {

  private final WorkflowClientRequestFactory factory =
      new WorkflowClientRequestFactory(
          WorkflowClientOptions.newBuilder().validateAndBuildWithDefaults());

  @Test
  public void signalWithStartForwardsRequestId() {
    String expectedRequestId = "test-request-id";
    WorkflowOptions options =
        WorkflowOptions.newBuilder()
            .setTaskQueue("test-task-queue")
            .setRequestId(expectedRequestId)
            .build();

    StartWorkflowExecutionRequest.Builder startRequest =
        factory.newStartWorkflowExecutionRequest(
            "test-workflow-id",
            "TestWorkflowType",
            new Header(Collections.emptyMap()),
            options,
            null,
            null,
            null);

    assertEquals(expectedRequestId, startRequest.getRequestId());

    SignalWithStartWorkflowExecutionRequest.Builder signalWithStartRequest =
        factory.newSignalWithStartWorkflowExecutionRequest(startRequest, "test-signal", null);

    assertEquals(expectedRequestId, signalWithStartRequest.getRequestId());
  }

  @Test
  public void signalWithStartGeneratesRequestIdWhenNotSet() {
    WorkflowOptions options = WorkflowOptions.newBuilder().setTaskQueue("test-task-queue").build();

    StartWorkflowExecutionRequest.Builder startRequest =
        factory.newStartWorkflowExecutionRequest(
            "test-workflow-id",
            "TestWorkflowType",
            new Header(Collections.emptyMap()),
            options,
            null,
            null,
            null);

    SignalWithStartWorkflowExecutionRequest.Builder signalWithStartRequest =
        factory.newSignalWithStartWorkflowExecutionRequest(startRequest, "test-signal", null);

    assertFalse(signalWithStartRequest.getRequestId().isEmpty());
  }
}
