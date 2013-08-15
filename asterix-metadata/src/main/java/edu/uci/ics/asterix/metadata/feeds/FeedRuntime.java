package edu.uci.ics.asterix.metadata.feeds;

import java.nio.ByteBuffer;

import edu.uci.ics.hyracks.api.comm.IFrameWriter;

public class FeedRuntime {

    public enum FeedRuntimeType {
        INGESTION,
        COMPUTE,
        STORAGE,
        COMMIT
    }

    protected final FeedRuntimeId feedRuntimeId;

    protected FeedRuntimeState runtimeState;

    public FeedRuntime(FeedConnectionId feedId, int partition, FeedRuntimeType feedRuntimeType) {
        this.feedRuntimeId = new FeedRuntimeId(feedRuntimeType, feedId, partition);
    }

    public FeedRuntime(FeedConnectionId feedId, int partition, FeedRuntimeType feedRuntimeType,
            FeedRuntimeState runtimeState) {
        this.feedRuntimeId = new FeedRuntimeId(feedRuntimeType, feedId, partition);
        this.runtimeState = runtimeState;
    }

    @Override
    public String toString() {
        return feedRuntimeId + " " + "runtime state ? " + (runtimeState != null);
    }

    public static class FeedRuntimeState {

        private ByteBuffer frame;
        private IFrameWriter frameWriter;
        private Exception exception;

        public FeedRuntimeState(ByteBuffer frame, IFrameWriter frameWriter, Exception e) {
            this.frame = frame;
            this.frameWriter = frameWriter;
            this.exception = exception;
        }

        public ByteBuffer getFrame() {
            return frame;
        }

        public void setFrame(ByteBuffer frame) {
            this.frame = frame;
        }

        public IFrameWriter getFrameWriter() {
            return frameWriter;
        }

        public void setFrameWriter(IFrameWriter frameWriter) {
            this.frameWriter = frameWriter;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

    }

    public static class FeedRuntimeId {

        private final FeedRuntimeType feedRuntimeType;
        private final FeedConnectionId feedId;
        private final int partition;
        private final int hashCode;

        public FeedRuntimeId(FeedRuntimeType runtimeType, FeedConnectionId feedId, int partition) {
            this.feedRuntimeType = runtimeType;
            this.feedId = feedId;
            this.partition = partition;
            this.hashCode = (feedId + "[" + partition + "]" + feedRuntimeType).hashCode();
        }

        @Override
        public String toString() {
            return feedId + "[" + partition + "]" + " " + feedRuntimeType;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof FeedRuntimeId) {
                FeedRuntimeId oid = ((FeedRuntimeId) o);
                return oid.getFeedId().equals(feedId) && oid.getFeedRuntimeType().equals(feedRuntimeType)
                        && oid.getPartition() == partition;
            }
            return false;
        }

        public FeedRuntimeType getFeedRuntimeType() {
            return feedRuntimeType;
        }

        public FeedConnectionId getFeedId() {
            return feedId;
        }

        public int getPartition() {
            return partition;
        }

    }

    public FeedRuntimeState getRuntimeState() {
        return runtimeState;
    }

    public void setRuntimeState(FeedRuntimeState runtimeState) {
        this.runtimeState = runtimeState;
    }

    public FeedRuntimeId getFeedRuntimeId() {
        return feedRuntimeId;
    }

}