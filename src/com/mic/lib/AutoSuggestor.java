package com.mic.lib;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

public class AutoSuggestor {
	public static CompletionProvider createCompletionProvider(String[] completes) {

	      // A DefaultCompletionProvider is the simplest concrete implementation
	      // of CompletionProvider. This provider has no understanding of
	      // language semantics. It simply checks the text entered up to the
	      // caret position for a match against known completions. This is all
	      // that is needed in the majority of cases.
	      DefaultCompletionProvider provider = new DefaultCompletionProvider();

	      
	      for(String s: completes) {
	    	  provider.addCompletion(new BasicCompletion(provider, s));
	      }
	      
	      
//	      // Add completions for all Java keywords. A BasicCompletion is just
//	      // a straightforward word completion.
//	      provider.addCompletion(new BasicCompletion(provider, "abstract"));
//	      provider.addCompletion(new BasicCompletion(provider, "assert"));
//	      provider.addCompletion(new BasicCompletion(provider, "break"));
//	      provider.addCompletion(new BasicCompletion(provider, "case"));
//	      // ... etc ...
//	      provider.addCompletion(new BasicCompletion(provider, "transient"));
//	      provider.addCompletion(new BasicCompletion(provider, "try"));
//	      provider.addCompletion(new BasicCompletion(provider, "void"));
//	      provider.addCompletion(new BasicCompletion(provider, "volatile"));
//	      provider.addCompletion(new BasicCompletion(provider, "while"));

//	      // Add a couple of "shorthand" completions. These completions don't
//	      // require the input text to be the same thing as the replacement text.
//	      provider.addCompletion(new ShorthandCompletion(provider, "sysout",
//	            "System.out.println(", "System.out.println("));
//	      provider.addCompletion(new ShorthandCompletion(provider, "syserr",
//	            "System.err.println(", "System.err.println("));

	      return provider;

	   }
    
}