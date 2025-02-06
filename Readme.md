# Setup
Open the file 'build.gradle.settings', IntelliJ will ask to link the project with gradle, do it!


open the file `Dtl.bnf`, right click and chose `Generate Parser` 
Note: if the option is not present, IntelliJ  propose to install `Grammar-Kit` plugin from MarketPlace (https://plugins.jetbrains.com/), do it!
and the option will be present.

# Versioning
The plugin version must be changed in the `plugin.xml` configuration file.
However, the jar/zip 
file name can be changed using the name and version in the gradle configuration file 

# Build
On the right when the project is loaded you should have some task, you can do directly build.
The plugin will be in 'build/distributions/DtlPlugin-2.1.9.zip' 

# Release
First to try to upload , please exec the task `intelliJ/verifiPlugin`

Inorder to release the plugin, you can go https://plugins.jetbrains.com/plugin/20024-dtl and sign-in as `anritsu.mediation@gmail.com` 
JetBrains Account with email, the password is in General-notes on Teams

Note:Is also possible to configure the task to upload the plugin form intelliJ

# Java Version
  Now we build with java 11, but we should use at least java 17, we can change it in 'build.gradle.settings'
  and also in the gradle compiler option eventually : IntelliJ -> Preferences -> Build, Execution, Deployment -> Build tools -> Maven -> Gradle


## High level steps for plugin.xml
- Define `<fileType name="..." implementationClass="..." fieldName="INSTANCE" language="xxx" extensions="..."/>`
- Define `<lang.parserDefinition language="xxx" implementationClass="..."/>`
- Define `<lang.syntaxHighlighterFactory language="xxx" implementationClass="..."/>`

## High level steps for grammar
- Define a `lexer.flex` file (usually inside `grammar` subfolder). Right click on it and select `Run JFlex Generator` - this will create the files needed by the grammar in order to parse the tokens defined in the `.flex` file.
- Define a `yyy.bnf` file (usually inside `grammar` subfolder).

## Plugin.xml definition
Each tag (`<annotator>`, `<completion.contributor>`, `<liveTemplateMacro>`, etc.) has to be _bound_ to an `implementationClass`, which usually implements a base `com.intellij.lang.x` interface (e.g. `xxxAnnotator` will implement `com.intellij.lang.annotation.Annotator`).

The base ones, `fileType` and `parserDefinition`, need to be implemented in order to correctly parse the file grammar, and identify the file as the one we want to parse. They are very simple, and just need to provide a name and a description (in case of file, also an extension), and optionally some icons.

**Please remember to add a**
```
@NotNull
public TokenSet getWhitespaceTokens() {
    return DtlTokenSet.DTL_WS_SET;
}
```
**in `parserDefinition` implementation, in order to correctly identify whitespaces.**

The other one are more specific, and thus need to follow the guidelines reported below in the [Useful links](#Useful links).

# Highlighter
When you put `<lang.syntaxHighlighterFactory language="xxx" implementationClass="..."/>` in the pluginXml, the implementation class has to be defined as a Factory which extends `SyntaxHighlighterFactory` and implements `getSyntaxHighlighter`. Usually (when you're not handling more than one file/extension), this just returns an instance of a custom highlighter class, which will in turn extend `SyntaxHighlighterBase`.

Here, you will tipically define some sets that has the same formatting, e.g.
```
    public static final TextAttributesKey DTL_COMMENT_SET = createTextAttributesKey("DTL_COMMENT_SET", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey DTL_KEYWORD_SET = createTextAttributesKey("DTL_KEYWORD_SET", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey DTL_STRING_SET = createTextAttributesKey("DTL_STRING_SET", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey DTL_NUMBER_SET = createTextAttributesKey("DTL_NUMBER_SET", DefaultLanguageHighlighterColors.NUMBER);
```
and then provide their respective keys
```
    private static final TextAttributesKey[] DTL_COMMENT_SET_KEYS = new TextAttributesKey[]{DTL_COMMENT_SET};
    private static final TextAttributesKey[] DTL_KEYWORD_SET_KEYS = new TextAttributesKey[]{DTL_KEYWORD_SET};
    private static final TextAttributesKey[] DTL_STRING_SET_KEYS = new TextAttributesKey[]{DTL_STRING_SET};
    private static final TextAttributesKey[] DTL_NUMBER_SET_KEYS = new TextAttributesKey[]{DTL_NUMBER_SET};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
```

You have to override the `getHighlightingLexer` method, which should just return a custom class extending `FlexAdapter`, and that's nothing more of a 
```
public class DtlLexerAdapter extends FlexAdapter {
  public DtlLexerAdapter() {
    super(new DtlLexer(null));
  }
}
```
where this `DtlLexer` is already created for us when we generate the `.flex` file.

Then the specific part comes: you'll override `getTokenHighlights`, and based on the `IElementType tokenType`, you can choose which formatting you want to apply on which set. E.g.
```
@Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (DtlTokenSet.DTL_STRING_SET.contains(tokenType)) {
            return DTL_STRING_SET_KEYS;
        }
        if (DtlTokenSet.DTL_COMMENT_SET.contains(tokenType)) {
            return DTL_COMMENT_SET_KEYS;
        }
        if (DtlTokenSet.DTL_KEYWORD_SET.contains(tokenType)) {
            return DTL_KEYWORD_SET_KEYS;
        }
        if (DtlTokenSet.DTL_NUMBER_SET.contains(tokenType)) {
            return DTL_NUMBER_SET_KEYS;
        }
        return EMPTY_KEYS;
    }
```

# Commenter
The commenter is a useful tool to let the IDE know when you're commenting or providing documentation. the tag for `plugin.xml` is `<lang.commenter language="xxx" implementationClass="..."/>` and in the provided `implementationClass` you will extend `CodeDocumentationAwareCommenter`, overriding the obvious methods there reported: `getLineCommentPrefix`, `getBlockCommentPrefix`, `getBlockCommentSuffix`, `getDocumentationCommentPrefix`, etc.

# Annotators
First of all, you can define more than one annotator in the `plugin.xml` file through several `<annotator language="xxx" implementationClass="...">`, optionally giving it an `id` and a `order`.

Each of the annotators will implement `Annotator` base interface, and thus has to provide `annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder)` method. Usually you want to annotate something only if it contains some error, and in order to catch it you'll have to determine the `PsiElement` type through `instanceof`.

The mechanism of an annotator is simple: it takes each token, and run `annotate` on it. On the other hand, if not handled correctly (for example, creating a Singleton which collects and stores the information you need, just once) this can be very time-consuming for the IDE. The way to annotate something, though, it's pretty simple: you create a `annotationHolder.newAnnotation(HighlightSeverity.xxx, "Some message to be displayed when hovering on the element").create();`.

# Completion
TODO

# Useful links

http://www.jetbrains.org/intellij/sdk/docs/tutorials/custom_language_support_tutorial.html

https://upsource.jetbrains.com/idea-ce/file/idea-ce-d00d8b4ae3ed33097972b8a4286b336bf4ffcfab/java/java-psi-impl/src/com/intellij/lang/java/lexer/_JavaLexer.flex
