package com.commonsware.cwac.saferoom.test;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.SpannableStringBuilder;
import com.commonsware.cwac.saferoom.SafeHelperFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.File;
import java.io.IOException;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class WALTest {
  private static final String DB_NAME="db";
  private static final String PASSPHRASE="Call me Ishmael. Some years ago—never mind how long precisely—having little or no money in my purse, and nothing particular to interest me on shore, I thought I would sail about a little and see the watery part of the world. It is a way I have of driving off the spleen and regulating the circulation. Whenever I find myself growing grim about the mouth; whenever it is a damp, drizzly November in my soul; whenever I find myself involuntarily pausing before coffin warehouses, and bringing up the rear of every funeral I meet; and especially whenever my hypos get such an upper hand of me, that it requires a strong moral principle to prevent me from deliberately stepping into the street, and methodically knocking people’s hats off—then, I account it high time to get to sea as soon as I can. This is my substitute for pistol and ball. With a philosophical flourish Cato throws himself upon his sword; I quietly take to the ship. There is nothing surprising in this. If they but knew it, almost all men in their degree, some time or other, cherish very nearly the same feelings towards the ocean with me.";

  @After
  public void tearDown() {
    Context ctxt=InstrumentationRegistry.getTargetContext();
    File db=ctxt.getDatabasePath(DB_NAME);

    for (File f : db.getParentFile().listFiles()) {
      f.delete();
    }
  }

  @Test
  public void wal() throws IOException {
    SafeHelperFactory factory=
      SafeHelperFactory.fromUser(new SpannableStringBuilder(PASSPHRASE));
    SupportSQLiteOpenHelper helper=
      factory.create(InstrumentationRegistry.getTargetContext(), DB_NAME, 1,
        new Callback(1));
    SupportSQLiteDatabase db=helper.getWritableDatabase();

    assertFalse(db.isWriteAheadLoggingEnabled());

    assertTrue(db.enableWriteAheadLogging());
    assertTrue(db.isWriteAheadLoggingEnabled());

    db.close();

    factory=SafeHelperFactory.fromUser(new SpannableStringBuilder(PASSPHRASE));
    helper=
      factory.create(InstrumentationRegistry.getTargetContext(), DB_NAME, 1,
        new Callback(1));
    db=helper.getWritableDatabase();

    assertTrue(db.isWriteAheadLoggingEnabled());
    db.disableWriteAheadLogging();
    assertFalse(db.isWriteAheadLoggingEnabled());

    db.close();
  }

  private static final class Callback extends SupportSQLiteOpenHelper.Callback {
    public Callback(int version) {
      super(version);
    }

    @Override
    public void onCreate(SupportSQLiteDatabase db) {
      db.execSQL("CREATE TABLE foo (bar, goo);");
      db.execSQL("INSERT INTO foo (bar, goo) VALUES (?, ?)",
        new Object[] {1, "two"});
    }

    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion,
                          int newVersion) {

    }
  }
}
