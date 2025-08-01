package dev.relism.Zyra;

import dev.relism.Zyra.parser.Parser;
import dev.relism.Zyra.test.Address;
import dev.relism.Zyra.test.Country;
import dev.relism.Zyra.test.Status;
import dev.relism.Zyra.test.User;
import dev.relism.Zyra.zod.ZodParser;

public class ZyraCore {
    public static void main(String[] args) {
        System.out.println(Parser.parseObject(User.class).generateTypeScriptDefinition());
        System.out.println(Parser.parseObject(Address.class).generateTypeScriptDefinition());
        System.out.println(Parser.parseObject(Country.class).generateTypeScriptDefinition());

        System.out.println(Parser.parseEnum(Status.class).generateTypeScriptDefinition());

        System.out.println("------------");
        System.out.println(ZodParser.generateZodSchema(User.class).zodObjectString());
        System.out.println(ZodParser.generateZodSchema(User.class).zodJsonSchema());
    }
}
