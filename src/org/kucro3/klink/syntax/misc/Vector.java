package org.kucro3.klink.syntax.misc;

import org.kucro3.klink.syntax.Sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Vector implements Cloneable, Parser {
    public Vector(String prefix, String suffix, String separator)
    {
        this(prefix, suffix, separator, Integer.MAX_VALUE);
    }

    public Vector(String prefix, String suffix, String separator, int limit)
    {
        this.prefix = Objects.requireNonNull(prefix, "prefix");
        this.suffix = Objects.requireNonNull(suffix, "suffix");
        this.separator = Objects.requireNonNull(separator, "separator");
        this.limit = limit;

        if(limit < 0)
            throw new IllegalArgumentException("limit");
    }

    @Override
    public Vector parse(Sequence seq)
    {
        reset();

        String content = record(seq.next()).trim();

        if(!content.startsWith(prefix))
        {
            this.result = Results.PATTERN_MISMATCH;
            return this;
        }

        if(content.endsWith(suffix))
        {
            content = content.substring(prefix.length(), content.length() - suffix.length());

            if(content.isEmpty())
                this.parsed = new String[0];
            else
                this.parsed = new String[] {content};

            this.result = Results.SUCCEEDED;
            return this;
        }

        StringBuilder buffer = new StringBuilder(content.substring(prefix.length()));
        while(true)
        {
            content = record(seq.next()).trim();
            if(content.endsWith(suffix))
            {
                buffer.append(" ").append(content.substring(0, content.length() - suffix.length()));
                break;
            }
            else
                buffer.append(" ").append(content);
        }

        String[] splitted = buffer.toString().split(separator);

        if(splitted.length > limit)
        {
            this.result = OUT_OF_LIMIT;
            return this;
        }

        this.parsed = new String[splitted.length];

        for(int i = 0; i < splitted.length; i++)
            this.parsed[i] = splitted[i].trim();

        this.result = Results.SUCCEEDED;

        return this;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public Vector setPrefix(String prefix)
    {
        return new Vector(prefix, suffix, separator);
    }

    public String getSuffix()
    {
        return suffix;
    }

    public Vector setSuffix(String suffix)
    {
        return new Vector(prefix, suffix, separator);
    }

    public String getSeparator()
    {
        return separator;
    }

    public Vector setSeparator(String separator)
    {
        return new Vector(prefix, suffix, separator);
    }

    public Vector set(String prefix, String suffix)
    {
        return set(prefix, suffix, separator);
    }

    public Vector set(String prefix, String suffix, String separator)
    {
        return new Vector(prefix, suffix, separator);
    }

    @Override
    public Result getResult()
    {
        return result;
    }

    public String[] getLastParsed()
    {
        return parsed;
    }

    public boolean hasLastParsed()
    {
        return parsed != null;
    }

    public List<String> getLastReaded()
    {
        return Collections.unmodifiableList(lastReaded);
    }

    String record(String readed)
    {
        lastReaded.add(readed);
        return readed;
    }

    @Override
    public void reset()
    {
        result = null;
        lastReaded.clear();
        parsed = null;
    }

    @Override
    public Vector clone()
    {
        return new Vector(prefix, suffix, separator);
    }

    private final int limit;

    private Result result;

    private final List<String> lastReaded = new ArrayList<>();

    private String[] parsed;

    private final String prefix;

    private final String suffix;

    private final String separator;

    public static final Result OUT_OF_LIMIT = Result.of("OUT_OF_LIMIT", "Too many contents", false);
}
