package org.zenframework.z8.server.base.table.system;

import java.util.Arrays;
import java.util.Collection;

import org.zenframework.z8.server.base.job.scheduler.Scheduler;
import org.zenframework.z8.server.base.query.Query;
import org.zenframework.z8.server.base.table.Table;
import org.zenframework.z8.server.base.table.value.BoolField;
import org.zenframework.z8.server.base.table.value.DatetimeField;
import org.zenframework.z8.server.base.table.value.Field;
import org.zenframework.z8.server.base.table.value.IntegerField;
import org.zenframework.z8.server.base.table.value.Link;
import org.zenframework.z8.server.resources.Resources;
import org.zenframework.z8.server.runtime.IObject;
import org.zenframework.z8.server.types.bool;
import org.zenframework.z8.server.types.exception;
import org.zenframework.z8.server.types.guid;
import org.zenframework.z8.server.types.integer;

public class SchedulerJobs extends Table {
	static public String TableName = "SystemTasks";
	static public int MinRepeat = 10;
	static public int DefaultRepeat = 3600;

	static public class names {
		public final static String Jobs = "Jobs";
		public final static String Users = "Users";
		public final static String Job = "Job";
		public final static String User = "User";
		public final static String From = "From";
		public final static String Till = "Till";
		public final static String Repeat = "Repeat";
		public final static String Active = "Active";
		public final static String LastStarted = "LastStarted";
	}

	static public class strings {
		public final static String Title = "SchedulerJobs.title";
		public final static String Settings = "SchedulerJobs.settings";
		public final static String From = "SchedulerJobs.from";
		public final static String Till = "SchedulerJobs.till";
		public final static String Repeat = "SchedulerJobs.repeat";
		public final static String Active = "SchedulerJobs.active";
		public final static String LastStarted = "SchedulerJobs.lastStarted";
		public final static String ErrorNoJob = "SchedulerJobs.error.noJob";
	}

	public static class CLASS<T extends Table> extends Table.CLASS<T> {
		public CLASS() {
			this(null);
		}

		public CLASS(IObject container) {
			super(container);
			setJavaClass(SchedulerJobs.class);
			setName(TableName);
			setDisplayName(Resources.get(SchedulerJobs.strings.Title));
		}

		@Override
		public Object newObject(IObject container) {
			return new SchedulerJobs(container);
		}
	}

	public Jobs.CLASS<Jobs> jobs = new Jobs.CLASS<Jobs>(this);
	public Users.CLASS<Users> users = new Users.CLASS<Users>(this);

	public Link.CLASS<Link> job = new Link.CLASS<Link>(this);
	public Link.CLASS<Link> user = new Link.CLASS<Link>(this);

	public DatetimeField.CLASS<DatetimeField> from = new DatetimeField.CLASS<DatetimeField>(this);
	public DatetimeField.CLASS<DatetimeField> till = new DatetimeField.CLASS<DatetimeField>(this);
	public DatetimeField.CLASS<DatetimeField> lastStarted = new DatetimeField.CLASS<DatetimeField>(this);
	public IntegerField.CLASS<IntegerField> repeat = new IntegerField.CLASS<IntegerField>(this);
	public BoolField.CLASS<BoolField> active = new BoolField.CLASS<BoolField>(this);

	public SchedulerJobs(IObject container) {
		super(container);
	}

	@Override
	public void constructor2() {
		super.constructor2();

		jobs.setIndex("jobs");

		users.setIndex("users");

		description.setDisplayName(Resources.get(strings.Settings));

		job.setName(names.Job);
		job.setIndex("job");

		user.setName(names.User);
		user.setIndex("user");
		user.get().setDefault(Users.System);

		from.setName(names.From);
		from.setIndex("from");
		from.setDisplayName(Resources.get(strings.From));

		till.setName(names.Till);
		till.setIndex("till");
		till.setDisplayName(Resources.get(strings.Till));

		repeat.setName(names.Repeat);
		repeat.setIndex("repeat");
		repeat.setDisplayName(Resources.get(strings.Repeat));

		lastStarted.setName(names.LastStarted);
		lastStarted.setIndex("lastStarted");
		lastStarted.setDisplayName(Resources.get(strings.LastStarted));

		active.setName(names.Active);
		active.setIndex("active");
		active.setDisplayName(Resources.get(strings.Active));

		job.get().operatorAssign(jobs);
		user.get().operatorAssign(users);

		registerDataField(job);
		registerDataField(user);
		registerDataField(from);
		registerDataField(till);
		registerDataField(repeat);
		registerDataField(lastStarted);
		registerDataField(active);

		repeat.get().setDefault(new integer(DefaultRepeat));
		active.get().setDefault(new bool(true));

		registerFormField(jobs.get().name);
		registerFormField(users.get().name);
		registerFormField(description);
		registerFormField(from);
		registerFormField(till);
		registerFormField(repeat);
		registerFormField(lastStarted);
		registerFormField(active);

		queries.add(jobs);
		queries.add(users);

		links.add(job);
		links.add(user);
	}

	@Override
	protected void beforeCreate(Query data, guid recordId, guid parentId, Query model, guid modelRecordId) {
		super.beforeCreate(data, recordId, parentId, model, modelRecordId);
		setTaskDefaults();
	}

	@Override
	protected void afterCreate(Query data, guid recordId, guid parentId, Query model, guid modelRecordId) {
		super.afterCreate(data, recordId, parentId, model, modelRecordId);
		Scheduler.reset();
	}

	@Override
	protected void beforeUpdate(Query data, guid recordId, Collection<Field> fields, Query model, guid modelRecordId) {
		super.beforeUpdate(data, recordId, fields, model, modelRecordId);
		setTaskDefaults();
	}

	@Override
	protected void afterUpdate(Query data, guid recordId, Collection<Field> fields, Query model, guid modelRecordId) {
		super.afterUpdate(data, recordId, fields, model, modelRecordId);
		if (!fields.contains(lastStarted.get())) {
			Scheduler.reset();
		}
	}

	@Override
	protected void afterDestroy(Query data, guid recordId, Query model, guid modelRecordId) {
		super.afterDestroy(data, recordId, model, modelRecordId);
		Scheduler.reset();
	}

	private void setTaskDefaults() {
		Field jobField = job.get();
		if (!jobField.changed() || description.get().changed())
			return;
		guid jobId = jobField.guid();
		if (jobId.equals(guid.NULL))
			return;
		Jobs jobs = new Jobs.CLASS<Jobs>().get();
		if (!jobs.readRecord(jobId, Arrays.<Field> asList(jobs.description.get())))
			throw new exception(Resources.get(strings.ErrorNoJob));
		description.get().set(jobs.description.get().string());
	}

}