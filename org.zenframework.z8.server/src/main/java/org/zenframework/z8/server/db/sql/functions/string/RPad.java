package org.zenframework.z8.server.db.sql.functions.string;

import java.util.Collection;

import org.zenframework.z8.server.base.table.value.IField;
import org.zenframework.z8.server.db.DatabaseVendor;
import org.zenframework.z8.server.db.sql.FormatOptions;
import org.zenframework.z8.server.db.sql.SqlConst;
import org.zenframework.z8.server.db.sql.SqlToken;
import org.zenframework.z8.server.exceptions.db.UnknownDatabaseException;
import org.zenframework.z8.server.types.string;

public class RPad extends StringFunction {
	private SqlToken string;
	private SqlToken length;
	private SqlToken pad;

	public RPad(SqlToken string, SqlToken length, SqlToken pad) {
		this.string = string;
		this.length = length;
		this.pad = pad;
	}

	@Override
	public void collectFields(Collection<IField> fields) {
		string.collectFields(fields);
		length.collectFields(fields);
		pad.collectFields(fields);
	}

	@Override
	public String format(DatabaseVendor vendor, FormatOptions options, boolean logicalContext) {
		pad = (pad == null ? new SqlConst(new string(" ")) : pad);
		switch(vendor) {
		case Oracle:
			return "RPAD(" + string.format(vendor, options) + "," + length.format(vendor, options) + "," + pad.format(vendor, options) + ")";
		case SqlServer:
			return "LEFT(" + string.format(vendor, options) + "+REPLICATE(" + pad.format(vendor, options) + "," + length.format(vendor, options) + ")" + "," + length.format(vendor, options) + ")";
		case Postgres:
			return "rpad(" + string.format(vendor, options) + ", " + length.format(vendor, options) + ", " + pad.format(vendor, options) + ")";
		default:
			throw new UnknownDatabaseException();
		}
	}
}
