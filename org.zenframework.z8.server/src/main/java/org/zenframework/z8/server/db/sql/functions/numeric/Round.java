package org.zenframework.z8.server.db.sql.functions.numeric;

import java.util.Collection;

import org.zenframework.z8.server.base.table.value.IField;
import org.zenframework.z8.server.db.DatabaseVendor;
import org.zenframework.z8.server.db.FieldType;
import org.zenframework.z8.server.db.sql.FormatOptions;
import org.zenframework.z8.server.db.sql.SqlToken;
import org.zenframework.z8.server.exceptions.db.UnknownDatabaseException;

public class Round extends SqlToken {
	private SqlToken number;
	private SqlToken digits;

	public Round(SqlToken number, SqlToken digits) {
		this.number = number;
		this.digits = digits;
	}

	@Override
	public void collectFields(Collection<IField> fields) {
		number.collectFields(fields);
		digits.collectFields(fields);
	}

	@Override
	public String format(DatabaseVendor vendor, FormatOptions options, boolean logicalContext) {
		switch(vendor) {
		case Oracle:
		case Postgres:
			return "ROUND(CAST(" + number.format(vendor, options) + " AS numeric), " + (digits != null ? digits.format(vendor, options) : "0") + ")";
		case SqlServer:
			return "ROUND(" + number.format(vendor, options) + ", " + (digits != null ? digits.format(vendor, options) : "0") + ", 0)";
		default:
			throw new UnknownDatabaseException();
		}
	}

	@Override
	public FieldType type() {
		return number.type();
	}
}
