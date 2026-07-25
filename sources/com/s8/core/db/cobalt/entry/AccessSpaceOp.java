package com.s8.core.db.cobalt.entry;

import java.util.concurrent.CompletableFuture;

import com.s8.bohr.io.lithium.branches.LiBranch;
import com.s8.core.arch.silicon.async.MthProfile;
import com.s8.core.arch.titanium.databases.RequestDbMgOperation;
import com.s8.core.arch.titanium.handlers.h3.ConsumeResourceMgAsyncTask;
import com.s8.core.arch.titanium.handlers.h3.H3MgHandler;
import com.s8.meta.api.flow.S8User;
import com.s8.meta.api.flow.space.AccessSpaceS8Request;
import com.s8.meta.api.flow.space.AccessSpaceS8Response;
import com.s8.meta.api.flow.space.AccessSpaceS8Response.Status;
import com.s8.meta.api.flow.space.objects.SpaceS8Object;

/**
 * 
 * @author pierreconvert
 *
 */
class AccessSpaceOp extends RequestDbMgOperation<LiBranch> {





	public final MgSpaceHandler spaceHandler;



	/**
	 * 
	 */
	public final AccessSpaceS8Request request;



	public final CompletableFuture<AccessSpaceS8Response> future;





	/**
	 * 
	 * @param branchHandler
	 * @param onSucceed
	 * @param onFailed
	 */
	public AccessSpaceOp(long timestamp, S8User initiator, MgSpaceHandler spaceHandler, AccessSpaceS8Request request, 
			CompletableFuture<AccessSpaceS8Response> future) {
		super(timestamp, initiator);
		this.spaceHandler = spaceHandler;
		this.request = request;
		this.future = future;
	}


	@Override
	public ConsumeResourceMgAsyncTask<LiBranch> createAsyncTask() {
		return new ConsumeResourceMgAsyncTask<LiBranch>(spaceHandler) {


			@Override
			public MthProfile profile() { 
				return MthProfile.FX0; 
			}

			@Override
			public String describe() {
				return "CLONE-HEAD on "+spaceHandler.getIdentifier()+" branch of "+handler.getName()+ " repository";
			}

			@Override
			public boolean consumeResource(LiBranch branch) {
				boolean hasBeenModified = false;


				SpaceS8Object[] objects = branch.getCurrentExposure();


				future.complete(new AccessSpaceS8Response(Status.OK, objects));

				hasBeenModified = branch.getGraph().hasUnpublishedChanges();

				if(hasBeenModified && request.writeChangesImmediatelyAfter) {
					handler.save();
				}


				return hasBeenModified;


			}


			@Override
			public void catchException(Exception exception) {
				future.complete(new AccessSpaceS8Response(Status.INTERNAL_ERROR, null));
			}
		};
	}


	@Override
	public H3MgHandler<LiBranch> getHandler() {
		return spaceHandler;
	}

}
