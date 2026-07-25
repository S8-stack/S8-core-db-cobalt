package com.s8.core.db.cobalt.entry;

import java.util.concurrent.CompletableFuture;

import com.s8.bohr.io.lithium.branches.LiBranch;
import com.s8.core.arch.silicon.async.MthProfile;
import com.s8.core.arch.titanium.databases.RequestDbMgOperation;
import com.s8.core.arch.titanium.handlers.h3.ConsumeResourceMgAsyncTask;
import com.s8.meta.api.exceptions.S8IOException;
import com.s8.meta.api.flow.S8User;
import com.s8.meta.api.flow.space.ExposeSpaceS8Request;
import com.s8.meta.api.flow.space.ExposeSpaceS8Response;
import com.s8.meta.api.flow.space.ExposeSpaceS8Response.Status;

/**
 * 
 * @author pierreconvert
 *
 */
class ExposeObjectsOp extends RequestDbMgOperation<LiBranch> {




	/**
	 * 
	 */
	public final MgSpaceHandler spaceHandler;


	public final ExposeSpaceS8Request request;

	
	public final CompletableFuture<ExposeSpaceS8Response> future;





	/**
	 * 
	 * @param branchHandler
	 * @param onSucceed
	 * @param onFailed
	 */
	public ExposeObjectsOp(long timestamp, S8User initiator, MgSpaceHandler spaceHandler, ExposeSpaceS8Request request, 
			CompletableFuture<ExposeSpaceS8Response> future) {
		super(timestamp, initiator);
		this.spaceHandler = spaceHandler;
		this.request = request;
		this.future = future;
	}


	@Override
	public MgSpaceHandler getHandler() {
		return spaceHandler;
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
				return "CLONE-HEAD on "+spaceHandler.getIdentifier()+" branch of "+spaceHandler.getName()+ " repository";
			}

			@Override
			public boolean consumeResource(LiBranch branch) throws S8IOException {

				/* ranges */
				if(request.exposure != null) {
					int range = request.exposure.length;
					for(int slot = 0; slot < range; slot++) {
						branch.expose(slot, request.exposure[slot]);	
					}	
				}

				
				future.complete(new ExposeSpaceS8Response(Status.OK, 0x0L));
				
				if(request.saveImmediatelyAfter) {
					handler.save();
				}
				
				return true;
			}

			@Override
			public void catchException(Exception exception) {
				future.complete(new ExposeSpaceS8Response(Status.INTERNAL_EROR, 0x0L));
			}
		};
	}


}
