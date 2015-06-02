
input   = ARGV[0]
output  = input + ".ttl"

File.open(input,"r") do |f_in|
  File.open(output,"w") do |f_out|
  
       f_in.each_line do |line|

        if line.strip.empty? then next end

        match = /(\w+)\((\w+)-(\d+),\s*(\w+)-(\d+)\)\s*/.match(line)
        if match
    	
           uri_head  = "<token:" + match[3] + ">"
           uri_dep   = "<token:" + match[5] + ">"
           form_head = match[2].downcase
           form_dep  = match[4].downcase
           deprel    = match[1]
       
           f_out.write(uri_dep + " <conll:form> \""   + form_dep + "\" .\n")

           if form_head != "root" 
              f_out.write(uri_dep + " <conll:head> "     + uri_head + " .\n")
              f_out.write(uri_dep + " <conll:deprel> \"" + deprel + "\" .\n")
           end
        else
           raise "Input is not in Stanford format!"
        end
       end
  end
end
